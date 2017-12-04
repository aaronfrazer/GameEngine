#version 140

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform float contrast;

void main(void){

	out_Colour = texture(colourTexture, textureCoords);

	// Uncomment this to apply contrast
	out_Colour.rgb = (out_Colour.rgb - 0.5) * (1.0 + contrast) + 0.5;

    // Black and white effect
//    float col = (out_Colour.r + out_Colour.g + out_Colour.b) / 3;
//    out_Colour = vec4(col, col, col, 1.0);

}