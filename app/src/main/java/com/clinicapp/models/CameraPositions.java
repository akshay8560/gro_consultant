package com.clinicapp.models;

import android.util.Log;

import androidx.annotation.IntDef;

import com.clinicapp.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
public class CameraPositions {
    public float zoom;
    public final @Positions int position;
    public final String title;
    public static final int NO_OVERLAY = -1;

    @IntDef({ Positions.RIGHT, Positions.FRONTAL, Positions.LEFT, Positions.CROWN, Positions.VERTEX,
            Positions.HAIR_VERTEX, Positions.HAIR_VERTEX_ZOOM, Positions.HAIR_CROWN,
            Positions.HAIR_CROWN_ZOOM, Positions.HAIR_FRONTAL, Positions.HAIR_FRONTAL_ZOOM,
            Positions.HAIR_RIGHT, Positions.HAIR_RIGHT_ZOOM, Positions.HAIR_OCCIPITAL,
            Positions.HAIR_OCCIPITAL_ZOOM, Positions.HAIR_LEFT, Positions.HAIR_LEFT_ZOOM,

    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Positions {
        int RIGHT = 0,
            FRONTAL = 1,
            LEFT = 2,
            CROWN=3,
            VERTEX=4,
            HAIR_VERTEX=5,
            HAIR_VERTEX_ZOOM=6,
            HAIR_CROWN=7,
            HAIR_CROWN_ZOOM=8,
            HAIR_FRONTAL=9,
            HAIR_FRONTAL_ZOOM=10,
            HAIR_RIGHT=11,
            HAIR_RIGHT_ZOOM=12,
            HAIR_OCCIPITAL=13,
            HAIR_OCCIPITAL_ZOOM=14,
            HAIR_LEFT=15,
            HAIR_LEFT_ZOOM=16;
    }

    private CameraPositions(float zoom, @Positions int position, String title) {
        this.zoom = zoom;
        this.position = position;
        this.title = title;
    }

    public String getShootType(){
        switch (position){
            case Positions.HAIR_LEFT:
            case Positions.HAIR_RIGHT:
            case Positions.HAIR_VERTEX:
            case Positions.HAIR_OCCIPITAL:
            case Positions.HAIR_CROWN:
            case Positions.HAIR_FRONTAL:
            case Positions.HAIR_LEFT_ZOOM:
            case Positions.HAIR_RIGHT_ZOOM:
            case Positions.HAIR_VERTEX_ZOOM:
            case Positions.HAIR_OCCIPITAL_ZOOM:
            case Positions.HAIR_CROWN_ZOOM:
            case Positions.HAIR_FRONTAL_ZOOM:
                return zoom>0?"3x_closeup":"closeup";
            default:return "portrait";
        }
    }

    public int getImage(boolean isMale){
        switch (position){
            case Positions.HAIR_RIGHT:
            case Positions.HAIR_RIGHT_ZOOM:
            case Positions.RIGHT: return isMale?R.drawable.male_head_right:R.drawable.female_head_right;
            case Positions.HAIR_LEFT:
            case Positions.HAIR_LEFT_ZOOM:
            case Positions.LEFT: return isMale?R.drawable.male_head_left:R.drawable.female_head_left;
            case Positions.HAIR_FRONTAL:
            case Positions.HAIR_FRONTAL_ZOOM:
            case Positions.HAIR_VERTEX:
            case Positions.HAIR_VERTEX_ZOOM:
            case Positions.HAIR_CROWN:
            case Positions.HAIR_CROWN_ZOOM:
            case Positions.CROWN: return isMale?R.drawable.male_head_top:R.drawable.female_head_top;
            case Positions.VERTEX: return isMale?R.drawable.male_head_back:R.drawable.female_head_back;
            case Positions.HAIR_OCCIPITAL:return isMale?R.drawable.male_head_back:R.drawable.female_head_back;
            case Positions.HAIR_OCCIPITAL_ZOOM:return isMale?R.drawable.male_head_back:R.drawable.female_head_back;
            case Positions.FRONTAL:
            default: return isMale?R.drawable.male_head_frontal:R.drawable.female_head_frontal;
        }
    }

    public boolean isHeadVisible(){
        return this.position == Positions.HAIR_FRONTAL ||
               this.position == Positions.HAIR_FRONTAL_ZOOM ||
                this.position == Positions.HAIR_VERTEX ||
                this.position == Positions.HAIR_VERTEX_ZOOM ||
                this.position == Positions.HAIR_CROWN ||
                this.position == Positions.HAIR_CROWN_ZOOM;
    }

    public boolean isHairPosition(){
        return this.position>Positions.VERTEX;
    }


    public boolean isRightHeadVisible(){
        return this.position==Positions.HAIR_RIGHT ||
                this.position==Positions.HAIR_RIGHT_ZOOM;
    }

    public boolean isLeftHeadVisible(){
        return this.position==Positions.HAIR_LEFT ||
                this.position==Positions.HAIR_LEFT_ZOOM;
    }


    public boolean isFrontalChecked(){
        return this.position == Positions.HAIR_FRONTAL ||
                this.position == Positions.HAIR_FRONTAL_ZOOM ||
                this.position == Positions.HAIR_OCCIPITAL_ZOOM ||
                this.position == Positions.HAIR_OCCIPITAL;
    }

    public boolean isVertexChecked(){
        return this.position == Positions.HAIR_VERTEX ||
                this.position == Positions.HAIR_VERTEX_ZOOM;
    }

    public boolean isCrownChecked(){
        return this.position == Positions.HAIR_CROWN ||
                this.position == Positions.HAIR_CROWN_ZOOM ||
                this.position == Positions.HAIR_LEFT ||
                this.position == Positions.HAIR_LEFT_ZOOM ||
                this.position == Positions.HAIR_RIGHT ||
                this.position == Positions.HAIR_RIGHT_ZOOM ||
                this.position == Positions.HAIR_OCCIPITAL_ZOOM ||
                this.position == Positions.HAIR_OCCIPITAL;
    }


    public String getText(){
       return title;
    }

    public boolean shouldShowMarker(){
        return this.position == Positions.HAIR_OCCIPITAL || this.position == Positions.HAIR_OCCIPITAL_ZOOM
                || (this.position >= Positions.HAIR_VERTEX && this.position <= Positions.HAIR_FRONTAL_ZOOM);
    }

    public int getOverlay(){
        switch (position){
            case Positions.LEFT: return R.drawable.left_camera_overlay;
            case Positions.RIGHT: return R.drawable.right_camera_overlay;
            case Positions.CROWN: return R.drawable.crown_camera_overlay;
            case Positions.VERTEX:
            case Positions.FRONTAL: return R.drawable.frontal_camera_overlay;
            default: return R.drawable.hair_camera_overlay;
        }
    }
    public static ArrayList<CameraPositions> getCameraPositionArray(ArrayList<Integer> positions){
        ArrayList<CameraPositions> result = new ArrayList<>();
        for (@Positions int position:positions) {
            switch (position){
                case Positions.RIGHT:
                    result.add(new CameraPositions(0, position,"Right"));
                    break;
                case Positions.FRONTAL:
                    result.add(new CameraPositions(0, position,"Frontal"));
                    break;
                case Positions.LEFT:
                    result.add(new CameraPositions(0, position,"Left"));
                    break;
                case Positions.CROWN:
                    result.add(new CameraPositions(0, position,"Crown"));
                    break;

                case Positions.VERTEX:
                    result.add(new CameraPositions(0, position,"Vertex"));
                    break;
                case Positions.HAIR_VERTEX:
                    result.add(new CameraPositions(0, position,"Vertex"));
                    break;
                case Positions.HAIR_VERTEX_ZOOM:
                    result.add(new CameraPositions(3f, position,"Vertex (Zoomed)"));
                    break;
                case Positions.HAIR_CROWN:
                    result.add(new CameraPositions(0, position,"Crown"));
                    break;
                case Positions.HAIR_CROWN_ZOOM:
                    result.add(new CameraPositions(3f, position,"Crown (Zoomed)"));
                    break;
                case Positions.HAIR_FRONTAL:
                    result.add(new CameraPositions(0, position,"Frontal"));
                    break;
                case Positions.HAIR_FRONTAL_ZOOM:
                    result.add(new CameraPositions(3f, position,"Frontal (Zoomed)"));
                    break;
                case Positions.HAIR_RIGHT:
                    result.add(new CameraPositions(0, position,"Right"));
                    break;
                case Positions.HAIR_RIGHT_ZOOM:
                    result.add(new CameraPositions(3f, position,"Right (Zoomed)"));
                    break;
                case Positions.HAIR_OCCIPITAL:
                    result.add(new CameraPositions(0, position,"Occipital"));
                    break;
                case Positions.HAIR_OCCIPITAL_ZOOM:
                    result.add(new CameraPositions(3f, position,"Occipital (Zoomed)"));
                    break;
                case Positions.HAIR_LEFT:
                    result.add(new CameraPositions(0, position,"Left"));
                    break;
                case Positions.HAIR_LEFT_ZOOM:
                    result.add(new CameraPositions(3f, position,"Left (Zoomed)"));
                    break;
            }
        }
        return result;
    }

}