package com.nyuen.test_fivehundred.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PhotoPatternContainer {

    public enum Pattern {
        //First int = # of photos that a pattern can hold
        //Rest of the in = size of the image and should be loaded for each grid
        ONE(1, 4, 0, 0, 0),
        TWO_VERTICAL(2, 3, 3, 0, 0),
        TWO_HORIZONTAL(2, 3, 3, 0, 0),
        FOUR(4, 3, 3, 3, 3),
        THREE_VERTICAL(3, 3, 3, 3, 0),
        THREE_HORIZONTAL(3, 3, 3, 3, 0);

        private final int mImageCount;
        private final int[] mImageSizes;
        
        public int getCount() { return mImageCount; }

        Pattern(int i, int... sizes) {
            mImageCount = i;
            mImageSizes = sizes;
        }
        
        public int[] getSizes() {
            return mImageSizes;
        }
        
        public static List<Pattern> getPatternList() {
            List<Pattern> list = new ArrayList<Pattern>();
            list.add(Pattern.ONE);
            list.add(Pattern.TWO_VERTICAL);
            list.add(Pattern.TWO_HORIZONTAL);
            list.add(Pattern.FOUR);
            list.add(Pattern.THREE_VERTICAL);
            list.add(Pattern.THREE_HORIZONTAL);
            Collections.shuffle(list);
            return list;
        }
    }

    private Pattern mPattern;
    private List<Integer> mPhotosIdx;
    
    public PhotoPatternContainer(Pattern p) {
        setPattern(p);
        mPhotosIdx = new ArrayList<Integer>();
    }

    public Pattern getPattern() {
        return mPattern;
    }

    public void setPattern(Pattern mPattern) {
        this.mPattern = mPattern;
    }

    public List<Integer> getPhotosIdx() {
        return mPhotosIdx;
    }

    public void setPhotos(List<Integer> list) {
        this.mPhotosIdx = list;
    }
    
    public void addPhotoID(int i) {
        mPhotosIdx.add(i);
    }
    

}
