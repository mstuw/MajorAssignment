#version 330 core

#define TOTAL_POINT_LIGHTS 4
#define TOTAL_SPOT_LIGHTS 4
#define TOTAL_DIR_LIGHTS 4

out vec4 fragColor;

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
}; 

struct DirLight {
    bool enabled;
    
    vec3 direction;
	
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct PointLight {
    bool enabled;
    
    vec3 position;
    
    float constant;
    float linear;
    float quadratic;
	
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct SpotLight {
    bool enabled;

    vec3 position;
    vec3 direction;
    float cutOff;
    float outerCutOff;
  
    float constant;
    float linear;
    float quadratic;
  
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;       
};

in vec3 fragPos;
in vec3 normal;
in vec2 uv;

uniform vec3 viewPos;

uniform DirLight dirLights[TOTAL_DIR_LIGHTS];
uniform PointLight pointLights[TOTAL_POINT_LIGHTS];
uniform SpotLight spotLights[TOTAL_SPOT_LIGHTS];

uniform Material material;

vec3 calcDirLight(DirLight light, vec3 normal, vec3 viewDir);
vec3 calcPointLight(PointLight light, vec3 normal, vec3 viewDir);
vec3 calcSpotLight(SpotLight light, vec3 normal, vec3 viewDir);

float calcDiffuse(vec3 normal, vec3 lightDir);
float calcSpecular(vec3 lightDir, vec3 normal, vec3 viewDir);
float calcAttenuation(vec3 lightPosition, vec3 fragPos, float lightConstant, float lightLinear, float lightQuadratic);
vec3 combineResults(vec3 lightAmbient, vec3 lightDiffuse, vec3 lightSpecular, float diff, float spec, float multiplier);


void main() {    
    vec3 norm = normalize(normal);
    vec3 viewDir = normalize(viewPos - fragPos);
    
    vec3 result = vec3(0.0);
    
    // directional lighting
    for(int i = 0; i < TOTAL_DIR_LIGHTS; i++) {
        DirLight light = dirLights[i];
        if (light.enabled)
            result += calcDirLight(light, norm, viewDir);
    }
    
    // point lights
    for(int i = 0; i < TOTAL_POINT_LIGHTS; i++) {
        PointLight light = pointLights[i];
        if (light.enabled)
            result += calcPointLight(light, norm, viewDir);    
    }
    
    // spot light
    for(int i = 0; i < TOTAL_SPOT_LIGHTS; i++) {
        SpotLight light = spotLights[i];
        if (light.enabled)
            result += calcSpotLight(light, norm, viewDir);    
    }
    
    fragColor = vec4(result, 1.0);
}

vec3 calcDirLight(DirLight light, vec3 normal, vec3 viewDir) {

    vec3 lightDir = normalize(-light.direction);
    
    // diffuse shading
    float diff = calcDiffuse(normal, lightDir);
    
    // specular shading
    float spec = calcSpecular(lightDir, normal, viewDir);
   
    // combine results
    return combineResults(light.ambient, light.diffuse, light.specular, diff, spec, 1.0);    
}

vec3 calcPointLight(PointLight light, vec3 normal, vec3 viewDir) {

    vec3 lightDir = normalize(light.position - fragPos);
    
    // diffuse shading
    float diff = calcDiffuse(normal, lightDir);
    
    // specular shading
    float spec = calcSpecular(lightDir, normal, viewDir);
   
    // attenuation
    float attenuation = calcAttenuation(light.position, fragPos, light.constant, light.linear, light.quadratic);
     
    // combine results   
    return combineResults(light.ambient, light.diffuse, light.specular, diff, spec, attenuation);
}

vec3 calcSpotLight(SpotLight light, vec3 normal, vec3 viewDir) {

    vec3 lightDir = normalize(light.position - fragPos);
    
    // diffuse shading
    float diff = calcDiffuse(normal, lightDir);
    
    // specular shading
    float spec = calcSpecular(lightDir, normal, viewDir);
    
    // attenuation
    float attenuation = calcAttenuation(light.position, fragPos, light.constant, light.linear, light.quadratic);
   
    // spotlight intensity
    float theta = dot(lightDir, normalize(-light.direction)); 
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
    
    // combine results
    float ai = attenuation * intensity;         
    return combineResults(light.ambient, light.diffuse, light.specular, diff, spec, ai);
}

float calcDiffuse(vec3 normal, vec3 lightDir) {
    return max(dot(normal, lightDir), 0.0);
}

// Blinn-Phong specular
float calcSpecular(vec3 lightDir, vec3 normal, vec3 viewDir) {
    vec3 halfwayDir = normalize(lightDir + viewDir);
    
    return pow(max(dot(normal, halfwayDir), 0.0), material.shininess);
}

float calcAttenuation(vec3 lightPosition, vec3 fragPos, float lightConstant, float lightLinear, float lightQuadratic) {
    float distance = length(lightPosition - fragPos);
    return 1.0 / (lightConstant + lightLinear * distance + lightQuadratic * (distance * distance));      
}

vec3 combineResults(vec3 lightAmbient, vec3 lightDiffuse, vec3 lightSpecular, float diff, float spec, float multiplier) {
    vec3 ambient = lightAmbient * vec3(texture(material.diffuse, uv));
    vec3 diffuse = lightDiffuse * diff * vec3(texture(material.diffuse, uv));
    vec3 specular = lightSpecular * spec * vec3(texture(material.specular, uv));
    
    ambient *= multiplier;
    diffuse *= multiplier;
    specular *= multiplier;
    
    return (ambient + diffuse + specular);
}