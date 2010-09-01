package kozzion.graphic.sift;

import java.io.File;
import java.io.IOException;

public class NaiveSIFTMatcher {
	private static final double MATCH_THRESHOLD = 100000.0;

	public static int MatchSIFTFeatures(SIFTFeatures a, SIFTFeatures b) {
		int matches = 0;
		for (SIFTFeature aFeat : a.features) {
			double min = Double.MAX_VALUE;
			for (SIFTFeature bFeat : b.features) {
				double dist = matchSIFTFeature(aFeat,bFeat);
				if(dist < min) {
					min = dist;
				}
			}
			if(min < MATCH_THRESHOLD) {
				matches++;
			}
		}
		return matches;
	}

	private static double matchSIFTFeature(SIFTFeature a, SIFTFeature b) {
		double sum = 0;
		for (int i = 0; i < a.d; i++) {
			double d = a.descr[i] - b.descr[i];
			sum += d*d;
		}
		return sum;
	}
	
	public static void main(String[] args) throws IOException {
		SIFTFeatures a = SIFTFeatures.readFeatures(new File("./sift/nike.sift"));
		SIFTFeatures b = SIFTFeatures.readFeatures(new File("./sift/madonna.sift"));
		
		System.out.println("matched: " + NaiveSIFTMatcher.MatchSIFTFeatures(a, b));
	}
}
