/*
MyOpenLab by Carmelo Salafia www.myopenlab.de
Copyright (C) 2004  Carmelo Salafia cswi@gmx.de

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package CustomColorPicker;


import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
public class RoundGradientPaint implements Paint
{
    protected Point2D point;
    
    protected Point2D mRadius;
    
    protected Color mPointColor, mBackgroundColor;
    
    public RoundGradientPaint(double x, double y, Color pointColor,
            Point2D radius, Color backgroundColor)
    {
        if (radius.distance(0, 0) <= 0)
            throw new IllegalArgumentException("Radius must be greater than 0.");
        point = new Point2D.Double(x, y);
        mPointColor = pointColor;
        mRadius = radius;
        mBackgroundColor = backgroundColor;
    }
    
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
            Rectangle2D userBounds, AffineTransform xform, RenderingHints hints)
    {
        Point2D transformedPoint = xform.transform(point, null);
        Point2D transformedRadius = xform.deltaTransform(mRadius, null);
        return new RoundGradientContext(transformedPoint, mPointColor,
                transformedRadius, mBackgroundColor);
    }
    
    public int getTransparency()
    {
        int a1 = mPointColor.getAlpha();
        int a2 = mBackgroundColor.getAlpha();
        return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
    }
}
class RoundGradientContext implements PaintContext
{
    protected Point2D mPoint;
    
    protected Point2D mRadius;
    
    protected Color color1, color2;
    
    public RoundGradientContext(Point2D p, Color c1, Point2D r, Color c2)
    {
        mPoint = p;
        color1 = c1;
        mRadius = r;
        color2 = c2;
    }
    
    public void dispose()
    {
    }
    
    public ColorModel getColorModel()
    {
        return ColorModel.getRGBdefault();
    }
    
    public Raster getRaster(int x, int y, int w, int h)
    {
        WritableRaster raster = getColorModel().createCompatibleWritableRaster(w, h);
        
        int[] data = new int[w * h * 4];
        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                double distance = mPoint.distance(x + i, y + j);
                double radius = mRadius.distance(0, 0);
                double ratio = distance / radius;
                if (ratio > 1.0) ratio = 1.0;
                
                int base = (j * w + i) * 4;
                data[base + 0] = (int) (color1.getRed() + ratio  * (color2.getRed() - color1.getRed()));
                data[base + 1] = (int) (color1.getGreen() + ratio  * (color2.getGreen() - color1.getGreen()));
                data[base + 2] = (int) (color1.getBlue() + ratio * (color2.getBlue() - color1.getBlue()));
                data[base + 3] = (int) (color1.getAlpha() + ratio * (color2.getAlpha() - color1.getAlpha()));
            }
        }
        raster.setPixels(0, 0, w, h, data);
        
        return raster;
    }
}

