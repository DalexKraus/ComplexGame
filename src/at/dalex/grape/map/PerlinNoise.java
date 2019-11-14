package at.dalex.grape.map;

import java.util.Random;

public class PerlinNoise {

	private int seed;
	
	public PerlinNoise(int seed) {
		this.seed = seed;
	}
	
	public float[][] generateWhiteNoise(int xOffset, int yOffset, int width, int height) {
		Random random = new Random(seed);
		float[][] noise = GetEmptyArray(width, height);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				noise[i][j] = random.nextFloat() % 1;
			}
		}
		
		return noise;
	}

	float[][] generateSmoothNoise(float[][] baseNoise, int octave) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		
		float[][] smoothNoise = GetEmptyArray(width, height);
		
		int samplePeriod = 1 << octave;
		float sampleFrequency = 1f / samplePeriod;
		
		for (int i = 0; i < width; i++) {
			
			//Calculate horizontal sampling indices
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width; //Wrap around
			float horizontal_blend = (i - sample_i0) * sampleFrequency;
			
			for (int j = 0; j < height; j++) {
				//calculate the vertical sampling indices
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height; //Wrap around
				float vertical_blend = (j - sample_j0) * sampleFrequency;
				
				//Blend the top two corners
				float top = interpolate(baseNoise[sample_i0][sample_j0], baseNoise[sample_i1][sample_j0], horizontal_blend);
				
				//Blend the bottom two corners
				float bottom = interpolate(baseNoise[sample_i0][sample_j1], baseNoise[sample_i1][sample_j1], horizontal_blend);
				
				//Final blend
				smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);
			}
		}
		return smoothNoise;
	}
	
	public float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		
		float[][][] smoothNoise = new float[octaveCount][][]; //An array containing 2d arrays
		float persistance = 0.4f;
		
		//generate smooth noise
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = generateSmoothNoise(baseNoise, i);
		}
		
		float[][] perlinNoise = GetEmptyArray(width, height);
		
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;
		
		//blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}
		
		//Normalisation
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				perlinNoise[i][j] /= totalAmplitude;
			}
		}
		
		return perlinNoise;
	}
	
	public float[][] generatePerlinNoise(int xOffset, int yOffset, int width, int height, int octaveCount) {
		float[][] baseNoise = generateWhiteNoise(xOffset, yOffset, width, height);
		return generatePerlinNoise(baseNoise, octaveCount);
	}
	
	public float[][] GetEmptyArray(int width, int height) {
		return new float[width][height];
	}
	
	public float interpolate(float x0, float x1, float alpha) {
		return (x0 * (1 - alpha) + alpha * x1);
	}
}
