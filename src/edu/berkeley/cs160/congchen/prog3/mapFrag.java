package edu.berkeley.cs160.congchen.prog3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.app.Fragment;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class mapFrag extends Fragment implements OnTouchListener {
	
	private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 0.5f,MAX_ZOOM = 3.0f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
	
	
	/** (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            return null;
        }
		
		View v = inflater.inflate(R.layout.map, container, false);
		
        ImageView view = (ImageView) v.findViewById(R.id.bart_map);
        view.setOnTouchListener(this);
        
	    matrix = new Matrix();
	    savedMatrix = new Matrix();
	    
	    start = new PointF();
	    mid = new PointF();
	    oldDist = 1f;
	    
	    matrix.postScale(0.5f, 0.5f, mid.x, mid.y);
	    
	    view.setImageMatrix(matrix);
		
        return v;
	}
	
	@Override
    public boolean onTouch(View v, MotionEvent event) 
    {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        
        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) 
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                                                savedMatrix.set(matrix);
                                                start.set(event.getX(), event.getY());
                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
                                                mode = DRAG;
                                                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                                                mode = NONE;
                                                Log.d(TAG, "mode=NONE");
                                                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                                                oldDist = spacing(event);
                                                Log.d(TAG, "oldDist=" + oldDist);
                                                if (oldDist > 5f) {
                                                    savedMatrix.set(matrix);
                                                    midPoint(mid, event);
                                                    mode = ZOOM;
                                                    Log.d(TAG, "mode=ZOOM");
                                                }
                                                break;

            case MotionEvent.ACTION_MOVE:

                                                if (mode == DRAG) 
                                                { 
                                                    matrix.set(savedMatrix);
                                                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                                                } 
                                                else if (mode == ZOOM) 
                                                { 
                                                    // pinch zooming
                                                    float newDist = spacing(event);
                                                    Log.d(TAG, "newDist=" + newDist);
                                                    if (newDist > 5f) 
                                                    {
                                                        matrix.set(savedMatrix);
                                                        float zoomScale = newDist / oldDist;
                                                        
                                                        Matrix temp = new Matrix();
                                                        
                                                        temp.set(matrix);
                                                        temp.postScale(zoomScale, zoomScale, mid.x, mid.y);
                                                        
                                                        float[] f = new float[9];
                                                        temp.getValues(f);

                                                        float scaleX = f[Matrix.MSCALE_X];
                                                        float scaleY = f[Matrix.MSCALE_Y];
                                                        //float skewY = f[Matrix.MSKEW_Y];
                                                        float mapZoom = (float) Math.sqrt(scaleX * scaleX + scaleY * scaleY);
                       
                                                        if(mapZoom<MAX_ZOOM && mapZoom>MIN_ZOOM) 
                                                        {
                                                        	matrix.postScale(zoomScale, zoomScale, mid.x, mid.y); 
                                                        
                                                        }
                                                        
//                                                        scale = Math.max(MIN_ZOOM, Math.min(newDist / oldDist, MAX_ZOOM)); // setting the scaling of the
//                                                                                    // matrix...if scale > 1 means
//                                                                                    // zoom in...if scale < 1 means
//                                                                                    // zoom out
//                                                        matrix.postScale(scale, scale, mid.x, mid.y);
                                                    }
                                                }
                                                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) 
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) 
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) 
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) 
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }
}




