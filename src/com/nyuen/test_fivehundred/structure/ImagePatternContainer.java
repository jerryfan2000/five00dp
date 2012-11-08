package com.nyuen.test_fivehundred.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ImagePatternContainer {

    public enum Pattern {
        ONE(1),
        TWO_VERT(2),
        TWO_HOR(2),
        FOUR(4),
        THREE_VERT(3),
        THREE_HOR(3);

        private final int mImageCount;

        public int getCount() { return mImageCount; }

        Pattern(int i) {
            mImageCount = i;
        }
        
        public static List<Pattern> getPatternList() {
            List<Pattern> list = new ArrayList<Pattern>();
            list.add(Pattern.ONE);
            list.add(Pattern.TWO_VERT);
            list.add(Pattern.TWO_HOR);
            list.add(Pattern.FOUR);
            list.add(Pattern.THREE_VERT);
            list.add(Pattern.THREE_HOR);
//            list.add(Pattern.FOUR);
//            list.add(Pattern.FOUR);
//            list.add(Pattern.FOUR);
//            list.add(Pattern.FOUR);
//            list.add(Pattern.FOUR);
//            list.add(Pattern.FOUR);
            Collections.shuffle(list);
            return list;
        }
        
    }

    private Pattern mPattern;
    private List<Integer> mPhotosID;
    
    public ImagePatternContainer(Pattern p) {
        setPattern(p);
        mPhotosID = new ArrayList<Integer>();
    }

    public Pattern getPattern() {
        return mPattern;
    }

    public void setPattern(Pattern mPattern) {
        this.mPattern = mPattern;
    }

    public List<Integer> getPhotosID() {
        return mPhotosID;
    }

    public void setPhotos(List<Integer> list) {
        this.mPhotosID = list;
    }
    
    public void addPhotoID(int i) {
        mPhotosID.add(i);
    }
    

}
