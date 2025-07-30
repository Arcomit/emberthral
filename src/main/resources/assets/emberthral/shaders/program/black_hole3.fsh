#version 150
#define _PI_ 3.14159265359

uniform sampler2D DiffuseSampler;
uniform sampler2D Mask;
uniform vec2 wh_Len; // width / height, len

// 新增色相分离控制参数
//uniform float hueShiftAmount; // 色相分离强度 (0-1)
//uniform float hueShiftAngle; // 色相分离方向 (弧度制)
// uniform vec2 hueShiftVector; // 色相分离方向向量替代方案 (如果使用此项)

// 高斯模糊控制参数
//uniform float gaussianBlurAmount; // 模糊强度 (0.0-1.0)
//uniform float gaussianBlurRadius; // 模糊半径 (0.0-0.05)

in vec2 texCoord;
out vec4 fragColor;

float funcZ(float x){
    if(x < 0){
        return 0;
    }
    else{
        return 100 / 9 * x * x;
    }
}

float funcA(float x){
    return - 100 / 9 * (x-0.3) * (x-0.3) + 0.5;
}

float funcB(float x){
    return -25* (x-0.3) * (x-0.3) + 0.5;
}

float funcC(float x){
    return 25* (x-0.5) * (x-0.5);
}

// 新增：应用色相分离效果
vec3 applyHueShift(vec2 coord){
    // 计算分离偏移向量
    float radianAngle = 2.3561944902;
    vec2 shift = vec2(cos(radianAngle), sin(radianAngle)) * 0.015;


    // 分别采样不同通道的偏移纹理
    float r = texture(DiffuseSampler, coord - shift).r;
    float g = texture(DiffuseSampler, coord).g;
    float b = texture(DiffuseSampler, coord + shift).b;

    return vec3(r, g, b);
}

// 高斯模糊函数
vec3 applyGaussianBlur(vec2 coord, float radius) {
    // 高斯权重矩阵 (5x5简化版)
    float weights[5] = float[](0.0545, 0.2442, 0.4026, 0.2442, 0.0545);

    vec3 colorSum = vec3(0.0);
    float weightSum = 0.0;

    for (int x = -2; x <= 2; x++) {
        for (int y = -2; y <= 2; y++) {
            // 跳过中心点 (性能优化)
            if(x == 0 && y == 0) continue;

            // 计算偏移
            vec2 offset = vec2(float(x), float(y)) * radius;
            vec2 sampleCoord = coord + offset;

            // 计算权重（使用近似高斯分布）
            float weight = weights[abs(x) + 1] * weights[abs(y) + 1];

            // 采样并加权
            colorSum += texture(DiffuseSampler, sampleCoord).rgb * weight;
            weightSum += weight;
        }
    }

    // 添加中心像素并归一化
    vec3 centerColor = texture(DiffuseSampler, coord).rgb * weights[2] * weights[2];
    colorSum += centerColor;
    weightSum += weights[2] * weights[2];

    return colorSum / weightSum;
}

void main(){
    if(wh_Len.x < 0){
        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1);
        return;
    }

    vec4 offset = texture(Mask, texCoord);

    if(offset.a > 0.001){
        vec2 off = -offset.rg + vec2(0.5, 0.5);

        float l = sqrt(off.x * off.x + off.y * off.y);
        if(l > 0.001) {
            if(l < 0.15){
                l = funcZ(l) / l;
            }
            else if(l >= 0.15 && l<0.3){
                l = funcA(l) / l;
            }
            else if(l >= 0.3 && l<0.4){
                l = funcB(l) / l;
            }
            else{
                l = funcC(l) / l;
            }
        }
        else l = 0.0;
        off *= l;
        off.g = off.g * wh_Len.x;
        vec2 patchedCoord = texCoord + off * wh_Len.y * offset.a;

        // 在扭曲后应用色相分离
        vec3 color = applyHueShift(patchedCoord);

        // 应用高斯模糊
        // 计算实际模糊半径（基于效果强度）
        float blurRadius = 0.005 * 0.3 * offset.a;
        vec3 blurred = applyGaussianBlur(patchedCoord, blurRadius);
        // 混合模糊效果（根据强度参数）
        color = mix(color, blurred, 1);

        fragColor = vec4(color, 1);
    }
    else{
        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1);
    }
}