//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math.*;
import java.util.Stack.*;


/**
 *Canny is an algorithm to apply the canny edge detector to an image
 *@author:Timothy Sharman
 *@see code.iface.canny
 */

public class Canny extends Thread
{

  //the Gaussian smoothing operator used as part of the process.

  //GaussianSmooth gaussiansmooth;
 
  //The Contrast stretching operator used as part of the process

//  ContrastStretch contraststretch = new ContrastStretch();

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int[] dest_1d;

  /**
   *Applies the canny edge detector to the input image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param size The size of the kernel used in the smoothing
   *@param theta The gaussian smoothing standard deviation
   *@param lowthresh The low threshold for the tracking
   *@param highthresh The high threshold for the tracking
   *@param scale The amount of scaling to be applied to the image
   *@param offset The amount to be added to each result pixel
   *@return A pixel array containing edges in the image
   */

  //Tim's Canny Edge Detection Algorithm
  //Based on algorithm in Machine Vision (pg 169)
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) gaussian smooth image
    d) work out gradient magnitude
    e) apply nonmaxima suppression
    f) threshold and detect edges
    */

  public int [] apply_canny(int [] src_1d, int width, int height, int size, 
          float theta, int lowthresh, int highthresh, 
          float scale, int offset) {
    
    d_w = width;
    d_h = height;

    //Setup local variables
    dest_1d = new int[d_w*d_h];
    int [] tmp_1d = new int[d_w*d_h];
    int [][] tmp_2d = new int[d_w][d_h];
    int [][] p_2d = new int[d_w][d_h];
    int [][] q_2d = new int[d_w][d_h];
    int [][] m_2d = new int[d_w][d_h];
    double [][] theta_2d = new double[d_w][d_h];
    int [][] nms = new int[d_w][d_h];
    int [][] delta = new int[d_w][d_h];
    int [][] tracked = new int[d_w][d_h];
    int result;
    int tmp = 0;
    int [] tmp2_1d;

    
    //Set up the output array
    for(int i = 0; i < dest_1d.length; i++){
      dest_1d[i] = 0xff000000;
    }

    //Smooth the initial image
    //tmp_1d = gaussiansmooth. smooth_image(src_1d, width, height, size, theta);
    tmp_1d = src_1d;

    //Mask off so that we work with values between 0 and 255
    for(int i = 0; i < tmp_1d.length; i++){
      tmp_1d[i] = tmp_1d[i] & 0x000000ff;
    }
        
    //Convert 1_d to 2_d for ease of processing in next stages
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
  tmp_2d[i][j] = tmp_1d[i+(j*d_w)];
      }
    }

    //Apply the gradient detection
    for(int i = 0; i < (d_w-1); i++){
      for(int j = 0; j < (d_h-1); j++){
  p_2d[i][j] = (tmp_2d[i][j+1]-tmp_2d[i][j]+
          tmp_2d[i+1][j+1]-tmp_2d[i+1][j])/2;
  q_2d[i][j] = (tmp_2d[i][j]-tmp_2d[i+1][j]+
          tmp_2d[i][j+1]-tmp_2d[i+1][j+1])/2;
  m_2d[i][j] = (int)Math.sqrt(Math.pow((double)p_2d[i][j],2)+
            Math.pow((double)q_2d[i][j],2));
  theta_2d[i][j] = Math.atan2((double)(q_2d[i][j]),(double)(p_2d[i][j]));
      }
    }

    //Resize image 
    d_w--;
    d_h--;

    //Apply the nonmaxima suppression

    //First calculate which sector each line appears in

    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
  delta[i][j] = sector(theta_2d[i][j]);
      }
    }


    //Then apply non maximal suppression
    for(int i = 0; i < (d_w-1); i++){ nms[i][0] = 0; nms[i][d_h-1] = 0; }
    for(int j = 0; j < (d_h-1); j++){ nms[0][j] = 0; nms[d_w-1][j] = 0; }
    for(int i = 1; i < (d_w-1); i++){
      for(int j = 1; j < (d_h-1); j++){
  nms[i][j] = suppress(m_2d, delta[i][j], i, j,lowthresh);
      }
    }

    //Resize again!
    d_w = d_w - 2;
    d_h = d_h - 2;

    //Track the image
    tracked = apply_track(nms, d_w, d_h, lowthresh, highthresh);

    //Calculate the output array
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
  result = tracked[i][j];
  result = (int) (result * scale);
  result = result + offset;
  if(result > 255){result = 255;}
  if(result < 0){result = 0;}
  dest_1d[(i+(j*(d_w+3)))] = 0xff000000 | 
    result << 16 | result << 8 | result;
      }
    }
        
    //Change the sizes back
    d_w = d_w + 3;
    d_h = d_h + 3;

    return dest_1d;

  }

  //Function to check which sector the line is in (see Machine Vision pg 171)
  private int sector(double theta){
    
    //Converting into degrees from radians, and moving to lie between 0 and 360
    theta = Math.toDegrees(theta);
    theta = theta + 270 ; 
    theta = theta % 360;
    
    
    if((theta >= 337.5) || (theta < 22.5) || ((theta >= 157.5) && (theta < 202.5))){
      return 0;
    }
    if(((theta >= 22.5) && (theta < 67.5)) || ((theta >=202.5) && (theta < 247.5))){
      return 1;
    }
    if(((theta >= 67.5) && (theta < 112.5)) || ((theta >=247.5) && (theta < 292.5))){
      return 2;
    }
    if(((theta >= 112.5) && (theta < 157.5)) || ((theta >= 292.5) && (theta < 337.5))){
      return 3;
    }
    return 0;
  }
  
  // Function to apply non maxima suppression to the image array
  private int suppress(int[][] m_2d, int sector, int i, int j, int lowthresh){

    int tmp = m_2d[i][j];
    if (tmp < lowthresh) return 0;
    
//if (318 < i && i < 322 && 113 < j && j < 117)System.out.println("ij("+i+","+j+") sector: "+sector+" neigh: "+m_2d[i-1][j-1]+" "+m_2d[i-1][j]+" "+m_2d[i-1][j+1]+" "+m_2d[i][j-1]+" "+m_2d[i][j]+" "+m_2d[i][j+1]+" "+m_2d[i+1][j-1]+" "+m_2d[i+1][j]+" "+m_2d[i+1][j+1]);

    if(sector == 0){
      if((m_2d[i+1][j] >= tmp) || (m_2d[i-1][j] > tmp)){
  return 0;
      }
      else {
  return tmp;
      }
    }
    if(sector == 1){
      if((m_2d[i+1][j+1] >= tmp) || (m_2d[i-1][j-1] > tmp)){
  return 0;
      }
      else {
  return tmp;
      }
    }
    if(sector == 2){
      if((m_2d[i][j+1] >= tmp) || (m_2d[i][j-1] > tmp)){
  return 0;
      }
      else {
  return tmp;
      }
    }
    if(sector == 3){
      if((m_2d[i+1][j-1] >= tmp) || (m_2d[i-1][j+1] > tmp)){
  return 0;
      }
      else {
  return tmp;
      }
    }
    System.out.println("Canny - Unidentified sector "+sector+" at ij: "+i+" "+j);
    return 0;
  }

  /*The function apply_track is used to track the image for suitable lines. It
   *does this by first finding points above the highthreshold. When it finds
   *such a point it then finds surrounding point which are above the low threshold
   *and tracks along them, continually finding points above the low threshold. This
   *is done until the tracker explores all paths from the original point. It then
   *finds the next starting point and starts tracking again.
   */  
  
  private int [][] apply_track(int [][] input, int width, int height, 
             int lowthresh,int highthresh) {
    
    d_w = width;
    d_h = height;

    int [][] marked = new int[d_w][d_h];
    int [][] tracked = new int[d_w][d_h];
    
    Stack to_track = new Stack();
    
    //Initialise the marked array
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
  marked[i][j] = 0;
      }
    }
    
    //Now find all the starting points for the tracking
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
  //If the point is unmarked and above high threshold then track
  if((input[i][j] > highthresh) && (marked[i][j] == 0)){
    marked = track(input, marked, to_track, lowthresh, i, j);
  }
      }
    }
    
    //Now clear all the pixels in the input which are unmarked
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
  if(marked[i][j] == 0){
    tracked[i][j] = 0;
  }
  else {
    tracked[i][j] = input[i][j];
  }
      }
    }
    return tracked;
  }

  /*The function track is called once a starting point for tracking has been
   *found. When this happens, this function follows all possible paths above
   *the threshold by placing unsearched paths on the stack. Each time a path 
   *is looked at it's pixels are marked. This continues until the stack is
   *empty, at which point the new array of marked paths is returned.
   */

  private int [][] track(int [][] input, int [][] marked, Stack to_track, 
      int thresh, int i, int j){
    
    //empty represents when the stack is empty
    boolean empty = false;
    int a;
    int b;
    //Create a point to represent where to start the tracking from
    Point current = new Point(i,j);

    //Push the initial point onto the stack
    to_track. push(current);
    while(!empty){
      try{

  //Take the top pixel from the stack
  current = (Point)to_track. pop();
  //Find it's co-ordinates
  a = current.x;
  b = current.y;
  //Now check neighbourhood and add to stack anything above thresh
  //Only done if pixel is currently unmarked
  if(marked[a][b] == 0){
  
    //Try and track from each neighbouring point
    if(a > 0 && b > 0){
      if(input[a-1][b-1] > thresh){
        current = new Point((a-1), (b-1));
        to_track. push(current);
      }
    }
    
    if(b > 0){
      if(input[a][b-1] > thresh){
        current = new Point((a), (b-1));
        to_track. push(current);
      }
    }
    
    if(a < (d_w-1) && b > 0){
      if(input[a+1][b-1] > thresh){
        current = new Point((a+1), (b-1));
        to_track. push(current);
      }
    }
    
    if(a > 0){
      if(input[a-1][b] > thresh){
        current = new Point((a-1), (b));
        to_track. push(current);
      }
    }
    
    if(a < (d_w-1)){
      if(input[a+1][b] > thresh){
        current = new Point((a+1), (b));
        to_track. push(current);
      }
    }
    
    if(a > 0 && b < (d_h-1)){
      if(input[a-1][b+1] > thresh){
        current = new Point((a-1), (b+1));
        to_track. push(current);
      }
    }
    
    if( b < (d_h-1)){
      if(input[a][b+1] > thresh){
        current = new Point((a), (b+1));
        to_track. push(current);
      }
    }
    
    if(a < (d_w-1) && b < (d_h-1)){
      if(input[a+1][b+1] > thresh){
        current = new Point((a+1), (b+1));
        to_track. push(current);
      }
    }
    
    //Mark this pixel as having been tracked from
    marked[a][b] = 1;
  }
      } 
      
      //If stack empty, then set the empty flag to true
      catch(EmptyStackException e){
  empty = true;
      }
    
    }
    
    return marked;
  
  }


}
