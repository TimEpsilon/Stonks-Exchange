package fr.tim.smpbank;

import java.util.Random;

public class taux {
	
	public static float Tauxdx(float dx) {
		float m = -6000f;
		float M = 3000f;
		
		if (dx>0) {
			return (float)Math.pow(dx/M,1/3);
		}
		else {
			return (float)Math.pow(-dx/m,1/3);
		}
	}
	
	public static float Tauxjn(float jn) {
		float M = 12;
		
		float a = 6/M;
		float b = -6;
		
		return (float)Math.exp((double)a*jn+b);
	}
	
	public static float Tauxx(float x) {
		float a = (float)(Math.pow(10, -2)-Math.pow(10, -5));
		float b = 1f;
		
		return 1/(a*x+b);
	}
	
	public static float TauxTn(float Tn) {
		double s = -16/Math.log(0.3);
		
		if (Tn<=5) {
			return 1- (float)Math.exp(Math.pow(Tn-5,5)/(2*s)); 
		}
		else {
			return (float)Math.exp(-Math.pow(Tn-5,5)/(2*s)) -1;
		}
	}
	
	
	public static float newTaux(float tau,float Tn,float jn, float x, float dx) {
		float aTau  =0.2f;
		float aTn =0.155f;
		float aJn =0.1f;
		float aX =0.04f;
		float aDx =0.005f;
		
		float moyenne = (aTau*tau + aTn*TauxTn(Tn) + aJn*Tauxjn(jn) + aX*Tauxx(x) + aDx*Tauxdx(dx));
		float sigma = -0.09f*(float)Math.abs(TauxTn(Tn))+0.1f;
		
		Random r = new Random();
		float f = (float)r.nextGaussian()*sigma+moyenne;
		
		return Tn+f;
		
	}
	
}
