import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Event;
import java.awt.Point;
import java.awt.Image;
import java.lang.Math;


public class nomogram extends Applet implements ActionListener
{

/////////////////////////////////////////////////
////////////////////////////////////////////////
//variables use for initializing the nomogram
////////////////////////////////////////////////
////////////////////////////////////////////////

        //type of expression (mult, div, sub, etc...)
        int exType;

        int done;  //when user has finished entering the input

        //the distance between units in pixels
        double unitDistanceA, unitDistanceB, unitDistanceC ;


        int height=450;
        int width= 660;


        int lineSpace=60;

        //constants used in expression
        double constantA=1, constantB=1;
        //names for the variables in the expression
        String nameA= "A";
        String nameB= "B";
        String nameC= "C";
        //values for exponents in expression
        double exponentA=1;
        double exponentB=1;
        double exponentC=1;
        //names of the units to be used
        String unitsA, unitsB, unitsC;
        //min and max values for the variables
        double minA, minB, minC, maxA, maxB, maxC;

        //has the user entered values for the intervals?
        int intervalInputA=0, intervalInputB=0, intervalInputC=0;

        int logarithmic=0;  //are the scales logarithmic?


///////////////////////////////////////////////////
///////////////////////////////////////////////////
//variables used for drawing the nomogram
///////////////////////////////////////////////////
///////////////////////////////////////////////////

  //values of the 3 calculatable variables
  double valueA, valueB, valueC;
  //the points used for drawing the lines
  Point starts= new Point(0,0)
      , ends= new Point(0,0)
      , currentpoint= new Point(0,0);
  //values used to create the endpoints of the line
  double y1,y2,y3,y4;
  //the points at which the line intercepts the 4 axes
  Point intercept1, intercept2, intercept3, intercept4;
  //slope and y-intercept of the line
  double slope, B;
  //should the line be drawn in red?
  int redline=0;
  //initialize fonts to be used
  Font f= new Font("TimesRoman", Font.BOLD, 13);
  Font f1= new Font("Symbol", Font.PLAIN, 15);
  Font f2= new Font("TimesRoman",Font.PLAIN, 13);
  Font smallFont= new Font("TimesRoman", Font.PLAIN, 10);
  Font nameFont= new Font("TimesRoman", Font.BOLD, 14);
  Button printbutton;


////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
// separate windows which are used to get user input
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

        Frame getExpression;
	    Frame getInput;
	    Frame getInput2;
	    Frame restartFrame;



////////////////////////////////////////////////////
////////////////////////////////////////////////////
//buttons
////////////////////////////////////////////////////
////////////////////////////////////////////////////

		Button mult = new Button("a X b = c");

		Button addition = new Button("a + b = c");

		Button div = new Button("a / b = c");

		Button sub = new Button("a - b = c");

		Button five = new Button("a^m X b^n = c^p");


		Button okButton= new Button("                ok                ");

		Button okButton2= new Button("                ok                ");

		Button restart= new Button("restart");

		Button interval1Button= new Button("ok");

		Button interval2Button= new Button("ok");

		Button interval3Button= new Button("ok");

        Button closeWindow= new Button("Close this Window");

        Button closeWindow2= new Button("Close this Window");



///////////////////////////////////////////////////
///////////////////////////////////////////////////
//textfields
///////////////////////////////////////////////////
///////////////////////////////////////////////////

        TextField const1= new TextField(8);
        TextField const2= new TextField(8);

		TextField mText= new TextField(10);
		TextField nText= new TextField(10);
		TextField pText= new TextField(10);

		TextField aName= new TextField(20);
		TextField bName= new TextField(20);
		TextField cName= new TextField(20);

        TextField aUnits= new TextField(20);
        TextField bUnits= new TextField(20);
        TextField cUnits= new TextField(20);

        TextField aMinText, bMinText, cMinText, aMaxText, bMaxText, 
cMaxText;

        TextField intervalA, intervalB, intervalC;




////////////////////////////////////////////////
////////////////////////////////////////////////
//labels
////////////////////////////////////////////////
////////////////////////////////////////////////

		Label expressionLabel;



/////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////
//first function to be called, calls the begin function
///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////

public void init() {

        restartFrame= new Frame("choose intervals");
        restartFrame.setLayout(new FlowLayout());




		setLayout(new FlowLayout(FlowLayout.CENTER,0, height-20));

        begin();
  }



////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
//asks the user to choose an expression type
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

public void begin() {


        getExpression= new Frame("Choose expression type");
	    getInput= new Frame("User Input");
	    getInput2= new Frame("User Input");





        getExpression.setLayout(new FlowLayout());

	    Label label1= new Label("Choose type of expression:");
        Panel typeExp= new Panel();
        typeExp.add(label1);
		getExpression.add(typeExp);

        Panel choices= new Panel();
  		choices.add(mult);
  		mult.addActionListener(this);

		choices.add(addition);
		addition.addActionListener(this);

		choices.add(div);
		div.addActionListener(this);

		choices.add(sub);
		sub.addActionListener(this);

		choices.add(five);
		five.addActionListener(this);

		getExpression.add(choices);
		getExpression.resize(380, 200);
        getExpression.show();
        }



////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
//asks the user to input the variable names, constant values
//and exponent values if needed
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////

public void getUserInput() {


	    getInput.setLayout(new FlowLayout());


      Panel expression= new Panel();

	   expressionLabel = new Label("Enter parameters for expression of type");
        expression.add(expressionLabel);
        Label exp;
        switch (exType) {
          case 1:  exp= new Label("a X b = c");
                   logarithmic=1;
             break;
          case 2:  exp= new Label("a + b = c");
             break;
          case 3:  exp= new Label("a / b = c");
                   logarithmic=1;
             break;
          case 4:  exp= new Label("a - b = c");
             break;
          case 5:
          default: exp= new Label("a^m X b^n = c^p");
                   logarithmic=1;
          }
        expression.add(exp);
        getInput.add(expression);


		//Label blankspace= new Label("                                            
      ");
		//getInput.add(blankspace);

		Panel aConst= new Panel();
		Label Lconst1= new Label("Enter constant to multiply by a: ");
        aConst.add(Lconst1);
        const1.setText("1");
        aConst.add(const1);
        getInput.add(aConst);
        Panel bConst= new Panel();
        Label Lconst2= new Label("Enter constant to multiply by b: ");
        bConst.add(Lconst2);
        const2.setText("1");
        bConst.add(const2);
        getInput.add(bConst);

        //Label Lconst3= new Label("Enter constant to multiply by c: ");
        //getInput.add(Lconst3);
        //TextField const3= new TextField(8);
        //getInput.add(const3);
        //Label blankspace2= new Label("                                     
                         ");
		  //getInput.add(blankspace2);


		if (exType==5)
		{
		Panel expon1= new Panel();
		Label expM= new Label("Enter the value for the exponent m:");
		expon1.add(expM);
        mText.setText("1");
		expon1.add(mText);
		getInput.add(expon1);

		Panel expon2= new Panel();
		Label expN= new Label("Enter the value for the exponent n:");
		expon2.add(expN);
        nText.setText("1");
		expon2.add(nText);
		getInput.add(expon2);

      Panel expon3= new Panel();
		Label expP= new Label("Enter the value for the exponent p:");
		expon3.add(expP);
        pText.setText("1");
		expon3.add(pText);
		getInput.add(expon3);


		}

		Panel labelNames= new Panel();
		Label getLabels = new Label("Enter the names for variables a, b, and c 
(ex.:  distance)  ");
		labelNames.add(getLabels);
		getInput.add(labelNames);
		Panel Name1= new Panel();
		Label aLabel= new Label("Name for variable a:  ");
		Name1.add(aLabel);
        aName.setText("A");
		Name1.add(aName);
		getInput.add(Name1);

		Panel Name2= new Panel();
		Label bLabel= new Label("Name for variable b:  ");
		Name2.add(bLabel);
        bName.setText("B");
		Name2.add(bName);
		getInput.add(Name2);

		Panel Name3= new Panel();
		Label cLabel= new Label("Name for variable c:  ");
		Name3.add(cLabel);
        cName.setText("C");
		Name3.add(cName);
		getInput.add(Name3);





      Panel ok = new Panel();
		ok.add(okButton);
		okButton.addActionListener(this);
      getInput.add(ok);



        getInput.resize(380, 540);
        //getInput.pack();
        getInput.show();




}



///////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////
//retrieves the user input for the variable names and
//constant values, and stores it into variables
///////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////

public void storeData()
{


    Double constA,constB, expA, expB, expC;


     constA= new Double(const1.getText());
     constantA= constA.doubleValue();
     constB= new Double(const2.getText());
     constantB= constB.doubleValue();


if (exType==5)
	{
     expA= new Double(mText.getText());
     exponentA= expA.doubleValue();
     expB= new Double(nText.getText());
     exponentB= expB.doubleValue();
     expC= new Double(pText.getText());
     exponentC= expC.doubleValue();
    }


     nameA= new String(aName.getText());
     nameB= new String(bName.getText());
     nameC= new String(cName.getText());



    getUserInput2();
}



/////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////
//asks the user to enter the unit names
//and min and max values for the variables
/////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////

public void getUserInput2() {


     String op;

     switch (exType) {

       case 5:
       case 1: op= new String(" X ");
                break;
       case 2: op= new String(" + ");
                break;
       case 3: op= new String(" / ");
                break;
       default:
       case 4: op= new String(" - ");
       }




	 Label full_exp;

     if (exType==5)
      full_exp= new Label(""+constantA+" 
"+nameA+"^"+exponentA+op+constantB+" "+nameB+
                          "^"+exponentB+" = "+nameC+"^"+exponentC);
      else
	   full_exp= new Label(""+constantA +" "+ nameA + op +constantB+" "+nameB+" 
= " +nameC);

	 getInput2.setLayout(new FlowLayout());


	 getInput2.add(full_exp);


        Panel unitsLabel= new Panel();
        Label enterUnits= new Label("Enter the name of the units used for 
the variables (ex. kilometers)");
        unitsLabel.add(enterUnits);
        getInput2.add(unitsLabel);

        Panel units1= new Panel();
        Label aUnitsLabel= new Label("Units used for "+ nameA);
        units1.add(aUnitsLabel);
        units1.add(aUnits);

        Panel units2= new Panel();
        Label bUnitsLabel = new Label("Units used for "+ nameB);
        units2.add(bUnitsLabel);
        units2.add(bUnits);

        Panel units3= new Panel();
        Label cUnitsLabel= new Label("Units used for "+ nameC);
        units3.add(cUnitsLabel);
        units3.add(cUnits);


        getInput2.add(units1);
        getInput2.add(units2);
        getInput2.add(units3);



	 Panel Amin= new Panel();
	 Label getAmin= new Label("Enter minimum value for "+nameA);
	 aMinText= new TextField(10);
	 Amin.add(getAmin);
	 Amin.add(aMinText);
	 getInput2.add(Amin);
	 Panel Amax= new Panel();
	 Label getAmax= new Label("Enter maximum value for "+nameA);
	 aMaxText= new TextField(10);
	 Amax.add(getAmax);
	 Amax.add(aMaxText);
	 getInput2.add(Amax);


	 Panel Bmin= new Panel();
	 Label getBmin= new Label("Enter minimum value for "+nameB);
	 bMinText= new TextField(10);
	 Bmin.add(getBmin);
	 Bmin.add(bMinText);
	 getInput2.add(Bmin);
	 Panel Bmax= new Panel();
	 Label getBmax= new Label("Enter maximum value for "+nameB);
	 bMaxText= new TextField(10);
	 Bmax.add(getBmax);
	 Bmax.add(bMaxText);
	 getInput2.add(Bmax);

      Panel okPanel = new Panel();
	  okPanel.add(okButton2);
	  okButton2.addActionListener(this);
      getInput2.add(okPanel);

	 // getInput2.add(closeWindow2);

	 getInput2.resize(370, 540);
     getInput2.show();

}


///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
//retrieve the user input for the unit names and min/max values
//and put it into its proper data form
///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////

public void storeData2()
{
        int i;

///////////////////////////////////////////////////
//if addition, and one of the constants is negative
//then treat as subtraction
        if (exType==2)
          {if (constantA<0)
             if (constantB>=0)
              {exType=4;
               constantA= constantA*-1;
              }
            if (constantB<0)
              if (constantA>=0)
               {exType=4;
                constantB= constantB*-1;
                }
           }

////////////////////////////////////////////////////
//if subtraction and one of the constants is negative
//then treat as addition
        if (exType==4)
          {if (constantA<0)
             if (constantB>=0)
              {exType=2;
               constantA= constantA*-1;
              }
            if (constantB<0)
              if (constantA>=0)
               {exType=2;
                constantB= constantB*-1;
                }
           }





///////////////////////////////////////////////////
//get the names of the units out of the text boxes

        unitsA= new String(aUnits.getText());
        unitsB= new String(bUnits.getText());
        unitsC= new String(cUnits.getText());


//////////////////////////////////////////////////////////
//get the values for minA and maxA out of the text boxes

        Double aMinD= new Double(aMinText.getText());
        minA= aMinD.doubleValue();
        Double aMaxD= new Double(aMaxText.getText());
        maxA= aMaxD.doubleValue();

        if (minA==0)  minA=.1;


///////////////////////////////////////////////////
//if the scales are logarithmic, round off minA and
//maxA  to the nearest power of 10

        if (logarithmic==1)
         {
           if (minA<1)
             {
              i=-1;
              while (minA< Math.pow(10,i) )
               {
                i--;
               }
               minA= i;
             }
            else
             {
               i=0;
               while (minA>= Math.pow(10,i) )
                {
                 i++;
                }
                minA=  i-1;
              }

           if (maxA<1)
             {
              i=-1;
              while (maxA<= Math.pow(10,i) )
               {
                i--;
               }
               minA= i+1;
             }
            else
             {
               i=0;
               while (maxA> Math.pow(10,i) )
                {
                 i++;
                }
                maxA=  i;
              }

       }



////////////////////////////////////////////////////////
//get the values for minB and maxB out of the text boxes

        Double bMinD= new Double(bMinText.getText());
        minB= bMinD.doubleValue();
        Double bMaxD= new Double(bMaxText.getText());
        maxB= bMaxD.doubleValue();
        done=1;

        if (minB==0) minB=.1;

///////////////////////////////////////////////////
//if the scales are logarithmic, round off minB and
//maxB  to the nearest power of 10

        if (logarithmic==1)
         {
           if (minB<1)
             {
              i=-1;
              while (minB< Math.pow(10,i) )
               {
                i--;
               }
               minB= i;
             }
            else
             {
               i=0;
               while (minB>= Math.pow(10,i) )
                {
                 i++;
                }
                minB= i-1;
              }

           if (maxB<1)
             {
              i=-1;
              while (maxB<= Math.pow(10,i) )
               {
                i--;
               }
               minB= i+1;
             }
            else
             {
               i=0;
               while (maxB> Math.pow(10,i) )
                {
                 i++;
                }
                maxB=  i;
              }
}

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
// figure out the values for minC and max C
// based on the values for minA and maxA, and the expression type


         switch (exType) {
          case 1:
                   minC= 
constantA*Math.pow(10,minA)*constantB*Math.pow(10,minB);
                   maxC= 
constantA*Math.pow(10,maxA)*constantB*Math.pow(10,maxB);
             break;
          case 2:
                   minC= constantA*minA+constantB*minB;
                   maxC= constantA*maxA+constantB*maxB;
             break;
          case 3:
                   //if (minB!=0)
                    minC= 
(constantA*Math.pow(10,minA))/(constantB*Math.pow(10,maxB));
                   //else minC=0;
                   //if (maxB!=0)
                    maxC= 
(constantA*Math.pow(10,maxA))/(constantB*Math.pow(10,minB));
                   //else maxC=0;
             break;
          case 4:
                   minC= constantA*minA-constantB*maxB;
                   maxC= constantA*maxA-constantB*minB;
             break;
          case 5:
                   minC= Math.pow((
                           constantA* Math.pow( Math.pow(10,minA),exponentA)
                           * constantB* Math.pow( 
Math.pow(10,minB),exponentB)
                           )
                          , 1/exponentC);

                   maxC= Math.pow((
                          constantA* Math.pow( Math.pow(10,maxA),exponentA)
                          *constantB*Math.pow(Math.pow(10,maxB),exponentB)
                          )
                         , 1/exponentC);
                   break;
          default:
                   //minC= ( constantA*Math.pow(minA,exponentA) * 
constantB*Math.pow(minB,exponentB));
                   //maxC= ( constantA*Math.pow(maxA,exponentA) * 
constantB*Math.pow(maxB,exponentB));
                   minC=0;  maxC=0;
          }


///////////////////////////////////////////////////
//if the scales are logarithmic, round off minC and
//maxC  to the nearest power of 10

     if (logarithmic==1)
         {
           if (minC<1)
             {
              i=-1;
              while (minC< Math.pow(10,i) )
               {
                i--;
               }
               minC= i;
             }
            else
             {
               i=0;
               while (minC>= Math.pow(10,i) )
                {
                 i++;
                }
                minC=  i-1;
              }

           if (maxC<1)
             {
              i=-1;
              while (maxC<= Math.pow(10,i) )
               {
                i--;
               }
               maxC= i+1;
             }
            else
             {
               i=0;
               while (maxC> Math.pow(10,i) )
                {
                 i++;
                }
                maxC= i;
              }
}



////////////////////////////////
//create the frame which asks the user
//to enter the intervals
//
        intervalA= new TextField(8);
        intervalB= new TextField(8);
        intervalC= new TextField(8);

        interval1Button.addActionListener(this);
        interval2Button.addActionListener(this);
        interval3Button.addActionListener(this);
        closeWindow.addActionListener(this);


        Panel interval1P= new Panel();
        Label interval1Label= new Label("interval for "+nameA);
        interval1P.add(interval1Label);
        interval1P.add(intervalA);
        interval1P.add(interval1Button);

        Panel interval2P= new Panel();
        Label interval2Label= new Label("interval for "+nameB);
        interval2P.add(interval2Label);
        interval2P.add(intervalB);
        interval2P.add(interval2Button);


        Panel interval3= new Panel();
        Label interval3Label= new Label("interval for "+nameC);
        interval3.add(interval3Label);
        interval3.add(intervalC);
        interval3.add(interval3Button);


        restartFrame.add(interval1P);
        restartFrame.add(interval2P);
        restartFrame.add(interval3);

        restartFrame.add(closeWindow);



		//restart.addActionListener(this);

		restartFrame.resize(250,300);
		restartFrame.show();

        intervalA.addActionListener(this);
        intervalB.addActionListener(this);
        intervalC.addActionListener(this);

        done=1;
        repaint();
}






///////////////////////////////////////////////////////////
//function rounds off a number to its 3 significant digits
///////////////////////////////////////////////////////////
  public double roundOff(double num)
  {
   double d=num;
   int i=0;
   int negative=0;

   if (num<0)
   {
     d= num * -1;
     negative=1;
   }


   if (d>=1)
     {  while (d>Math.pow(10,i))
           i++;

         i--;
         //d= d- d%Math.pow(10,i-2);


         //System.out.println("the most significant digit is "+i);

         if (i>2)
            d= Math.round(d/Math.pow(10,i-2)) * Math.pow(10,i-2);
         if (i>0)
            d= (double)Math.rint(d*10)/10;
         if (i==0)
            d= (double)Math.rint(d*100)/100;
         //if (i==0)
         //   d= (double)Math.rint(d*1000)/1000;
          //if ( (num%Math.pow(10,i-2)) >=  (5*Math.pow(10,i-3) )  )
          // d+=  Math.pow(10,i-2);

           //d= (int) d;
           //d= (int)   d/Math.pow(10,i-2) * Math.pow(10,i-2);
      }
      else
      { i=0;
        while (d<Math.pow(10,i))
            i--;
         d= Math.round(d/Math.pow(10,i-2)) * Math.pow(10,i-2);
       }

      if (negative==1)
        d= d*-1;

      return  d;

   }





/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
//paint funtion-  used for drawing to the screen
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////

public void paint(Graphics g) {

  Font boldFont= new Font("TimesRoman", Font.BOLD, 15);
  Font normalFont= new Font("TimesRoman", Font.PLAIN, 13);
  g.setFont(boldFont);

//show the expression chosen by the user
     String op;

     switch (exType) {

       case 5:
       case 1: op= new String(" X ");
                break;
       case 2: op= new String(" + ");
                break;
       case 3: op= new String(" / ");
                break;
       default:
       case 4: op= new String(" - ");
       }


        //first, set up the basic background of the applet
        g.setColor(Color.white);
        g.fillRect(0, 0,1000,1000);

     g.setColor(Color.black);

     if (exType==5)
      g.drawString(""+constantA+" "+nameA+"^"+exponentA+op+constantB+" 
"+nameB+
                          "^"+exponentB+" = "+nameC+"^"+exponentC, 
width/2-100, 15);
      else
	   g.drawString(""+constantA +" "+ nameA + op +constantB+" "+nameB+" = " 
+nameC
	                    ,width/2-100, 15);









        g.setColor(Color.blue);
        //now, draw the line

        g.drawLine(starts.x, starts.y-1, currentpoint.x, currentpoint.y-1);
        g.drawLine(starts.x, starts.y, currentpoint.x, currentpoint.y);
        g.drawLine(starts.x, starts.y+1, currentpoint.x, currentpoint.y+1);
        g.drawLine(starts.x+1, starts.y, currentpoint.x+1, currentpoint.y);
        g.drawLine(starts.x-1, starts.y, currentpoint.x-1, currentpoint.y);
        //if line is finished being drawn by the user,
        //draw it in red
        g.setColor(Color.red);
        g.drawLine(starts.x, starts.y-1, ends.x, ends.y-1);
        g.drawLine(starts.x, starts.y, ends.x, ends.y);
        g.drawLine(starts.x, starts.y+1, ends.x, ends.y+1);
        g.drawLine(starts.x+1, starts.y, ends.x+1, ends.y);
        g.drawLine(starts.x-1, starts.y, ends.x-1, ends.y);





/////////////////////////////////////////////////////////
//if the user has finished entering the input
//then draw the scales
//
    if (done==1)
     {


        int minLocationA, minLocationB, minLocationC;
        int maxLocationA, maxLocationB, maxLocationC;

        g.setColor(Color.black);

        //draw left scale
        g.drawLine(lineSpace, lineSpace, lineSpace, height-lineSpace);
        g.drawLine(lineSpace-1, lineSpace, lineSpace-1, height-lineSpace);
        g.drawLine(lineSpace-10, lineSpace, lineSpace+10, lineSpace);
        g.drawLine(lineSpace-10, lineSpace-1, lineSpace+10, lineSpace-1);
        g.drawLine(lineSpace-10, height-lineSpace, lineSpace+10, 
height-lineSpace);
        g.drawLine(lineSpace-10, height-lineSpace+1, lineSpace+10, 
height-lineSpace+1);
        g.setFont(boldFont);
        g.drawString(nameA, lineSpace-10, height-lineSpace+15);
        g.setFont(normalFont);
        g.drawLine(lineSpace-5, height-lineSpace-10, lineSpace+5, 
height-lineSpace-10);
        minLocationA= height-lineSpace-10;
        if (logarithmic==1)
        {
        g.drawString("10", lineSpace+7, height-lineSpace-6);
        g.setFont(smallFont);
        g.drawString(""+(int)minA, lineSpace+20, height-lineSpace-12);
        g.setFont(normalFont);
        }
        else
        g.drawString(""+(int)minA, lineSpace+7, height-lineSpace-6);
        g.drawLine(lineSpace-5, lineSpace+10, lineSpace+5, lineSpace+10);
        if (logarithmic==1)
        {
        g.drawString("10", lineSpace+7, lineSpace+15);
        g.setFont(smallFont);
        g.drawString(""+(int)maxA, lineSpace+20, lineSpace+9);
        g.setFont(normalFont);
        }
        else
        g.drawString(""+(int)maxA, lineSpace+7, lineSpace+15);
        maxLocationA= lineSpace+10;


        //label the left scale
        double difference =  maxA- minA;
        double locationDifference= minLocationA-maxLocationA;
        double unitSpaceA=  locationDifference/difference;
        double interval;
        if (unitSpaceA > 20)
            interval=1;
            else
              interval=  (int)  (20/unitSpaceA);

        if (intervalInputA==1)
        {
        Double intervalD= new Double(intervalA.getText());
        interval= intervalD.doubleValue();
        }
         //g.drawString("interval is "+ interval, 30, 30);

         double intervalPosition;
         double i;

         //minLocationA= minLocationA+ 
(int)((minLocationA%interval)*unitSpaceA);

         int minInterval= (int) minA ;
            //(int) minA+ (int)minA%interval;


         for ( i=minInterval+interval; i<=maxA; i+=interval)
            {

                intervalPosition= minLocationA - (i-minA)*unitSpaceA;
              if (intervalPosition> maxLocationA+10)
               {
                g.drawLine(lineSpace-3, (int)(intervalPosition),
                           lineSpace+3, (int)(intervalPosition));
               if (logarithmic==1)
               {
                g.drawString("10", lineSpace+5, (int) (intervalPosition+5));
                g.setFont(smallFont);
                g.drawString(""+(int)i, lineSpace+18, (int) 
(intervalPosition-1));
                g.setFont(normalFont);
               }
               else
                g.drawString(""+(int)i, lineSpace+5, 
(int)(intervalPosition+5));
               }
              }





        //draw right scale
        g.drawLine(width-lineSpace, lineSpace, width-lineSpace, 
height-lineSpace);
        g.drawLine(width-lineSpace+1, lineSpace, width-lineSpace+1, 
height-lineSpace);
        g.drawLine(width-lineSpace-10, lineSpace, width-lineSpace+10, 
lineSpace);
        g.drawLine(width-lineSpace-10, lineSpace-1, width-lineSpace+10, 
lineSpace-1);
        g.drawLine(width-lineSpace-10, height-lineSpace, width-lineSpace+10, 
height-lineSpace);
        g.drawLine(width-lineSpace-10, height-lineSpace+1, 
width-lineSpace+10, height-lineSpace+1);
        g.setFont(boldFont);
        g.drawString(nameB, width-lineSpace-10, height-lineSpace+15);
        g.setFont(normalFont);


      switch (exType) {
        case 1: //multiplication
        case 5: //multiplication w/ exponents



        g.drawLine(width-lineSpace-5, height-lineSpace-10, 
width-lineSpace+5, height-lineSpace-10);
        minLocationB= height-lineSpace-10;
        g.drawString("10", width-lineSpace+7, height-lineSpace-6);
        g.setFont(smallFont);
        g.drawString(""+(int)minB, width-lineSpace+20, height-lineSpace-12);
        g.setFont(normalFont);

        g.drawLine(width-lineSpace-5, lineSpace+10, width-lineSpace+5, 
lineSpace+10);
        maxLocationB= lineSpace+10;
        g.drawString("10", width-lineSpace+7, lineSpace+15);
        g.setFont(smallFont);
        g.drawString(""+(int)maxB, width-lineSpace+20, lineSpace+9);
        g.setFont(normalFont);

        break;


        case 2: //addition

        g.drawLine(width-lineSpace-5, height-lineSpace-10, 
width-lineSpace+5, height-lineSpace-10);
        minLocationB= height-lineSpace-10;
        g.drawString(""+minB, width-lineSpace+7, height-lineSpace-6);
        g.drawLine(width-lineSpace-5, lineSpace+10, width-lineSpace+5, 
lineSpace+10);
        g.drawString(""+maxB, width-lineSpace+7, lineSpace+15);
        maxLocationB= lineSpace+10;


        break;

        case 3: //division



        g.drawLine(width-lineSpace-5, height-lineSpace-10, 
width-lineSpace+5, height-lineSpace-10);
        minLocationB= height-lineSpace-10;
        g.drawString("10", width-lineSpace+7, height-lineSpace-6);
        g.setFont(smallFont);
        g.drawString(""+(int)maxB, width-lineSpace+20, height-lineSpace-12);
        g.setFont(normalFont);

        g.drawLine(width-lineSpace-5, lineSpace+10, width-lineSpace+5, 
lineSpace+10);
        maxLocationB= lineSpace+10;
        g.drawString("10", width-lineSpace+7, lineSpace+15);
        g.setFont(smallFont);
        g.drawString(""+(int)minB, width-lineSpace+20, lineSpace+9);
        g.setFont(normalFont);

        break;


        case 4: //subtraction
        default:

        g.drawLine(width-lineSpace-5, height-lineSpace-10, 
width-lineSpace+5, height-lineSpace-10);
        minLocationB= height-lineSpace-10;
        g.drawString(""+maxB, width-lineSpace+7, height-lineSpace-6);
        g.drawLine(width-lineSpace-5, lineSpace+10, width-lineSpace+5, 
lineSpace+10);
        g.drawString(""+minB, width-lineSpace+7, lineSpace+15);
        maxLocationB= lineSpace+10;



        }




        //label the right scale
        difference =  maxB- minB;
        locationDifference= minLocationB-maxLocationB;
        double unitSpaceB=  locationDifference/difference;
        //int interval;




        if (unitSpaceB > 20)
            interval=1;
            else
              interval=  (int)  (20/unitSpaceB);


        if (intervalInputB==1)
        {
        Double intervalD= new Double(intervalB.getText());
        interval= intervalD.doubleValue();
        }


         //g.drawString("interval is "+ interval, 550, 30);




         minInterval= (int)minB;



         if (  (exType==4)  || (exType==3) )

            for ( i=(int)maxB-interval; i>=minB; i-=interval)
            {
                intervalPosition= minLocationB - (maxB-i)*unitSpaceB;
              if (intervalPosition> maxLocationB+10)
               {
                g.drawLine(width-lineSpace-3, (int)(intervalPosition),
                           width-lineSpace+3, (int)(intervalPosition));
                if (logarithmic==1)
               {
                g.drawString("10", width-lineSpace+5, (int) 
(intervalPosition+5));
                g.setFont(smallFont);
                g.drawString(""+(int)i, width-lineSpace+18, (int) 
(intervalPosition-1));
                g.setFont(normalFont);
               }
               else
                g.drawString(""+(int)i, width-lineSpace+5, 
(int)(intervalPosition+5));
               }
            }

         else
         for ( i=minInterval+interval; i<=maxB; i+=interval)
            {
                intervalPosition= minLocationB - (i-minB)*unitSpaceB;
              if (intervalPosition> maxLocationB+10)
               {
                g.drawLine(width-lineSpace-3, (int)(intervalPosition),
                           width-lineSpace+3, (int)(intervalPosition));
                if (logarithmic==1)
               {
                g.drawString("10", width-lineSpace+5, (int) 
(intervalPosition+5));
                g.setFont(smallFont);
                g.drawString(""+(int)i, width-lineSpace+18, (int) 
(intervalPosition-1));
                g.setFont(normalFont);
               }
               else
                g.drawString(""+(int)i, width-lineSpace+5, 
(int)(intervalPosition+5));
               }
            }




        //draw middle scale



        //create new variables which contain normal cartesian coordinates 
for
        //the actual pixel coordinates



        int maxLocB= height- lineSpace-10;
        int maxLocA= height- lineSpace-10;


        int minLocB=lineSpace+10;
        int minLocA=lineSpace+10;




        double slope1= (double)(  (double)(maxLocB- minLocA) /
                          (double)(width-lineSpace-lineSpace));
        double B1= (minLocB- slope1*lineSpace);
        //g.drawString("slope1 is "+slope1, width/2, 10);

        //yPosition which corresponds to the line which gets a value of 
minA+maxB
        double yPosition;


      switch (exType) {
        case 1: //multiplication

        yPosition= (double) (minLocA+ (double) unitSpaceA* (double)
                            (  maxB
                              - minB  )
                                      )  ;

        break;
        case 2: //addition

        yPosition=  (double)(minLocA+ unitSpaceA* ( 
maxB*constantB-minB*constantB)/constantA);
        break;

        case 3: //division

        yPosition= (double) (minLocA+  unitSpaceA*
                               (maxB
                                 - minB )
                                  );
         break;

        case 4: //subtraction

        yPosition=  (double)(minLocA+ unitSpaceA* ( 
maxB*constantB-minB*constantB)/constantA);
        break;

        default:
        case 5: //multiplication with exponents

        yPosition= (double) (minLocA+ (double) unitSpaceA* (double)
                            (  exponentB*maxB
                              - exponentB*minB  ) /exponentA
                                  )  ;


        break;

        }

        //g.drawString("y pos is "+yPosition, width/2, 25);
        double slope2= ( (double) (minLocB- yPosition) /
                         (double) (width-lineSpace-lineSpace));


        double B2=  (yPosition- slope2*lineSpace);


        g.setColor(Color.black);
        int mid=  (int)  (  (double)(B2-B1)/(double)(slope1- slope2) );




        g.setFont(normalFont);





        minLocationC= minLocationB;
        maxLocationC= maxLocationB;



///////////////////////////////////////////////////////////////////////////////////
//if it is a logarithmic scale, the endpoints of the middle scale need to be 
moved
//so that it lines up properly
//////////////////////////////////////////////////////////////////////////////////


     if ( (exType==1)|| (exType==5) )
      {
        double currentA=   minA ;
        double currentB=   minB ;
        double currentC;
               currentC= (double) (
                          exponentA*minA + Math.log(constantA)/Math.log(10) 
+
                         (double) exponentB*minB + 
Math.log(constantB)/Math.log(10)
                         )/exponentC;


         System.out.println("minA= "+minA);
         System.out.println("maxA= "+maxA);
         System.out.println(" log(constantA)= "+Math.log(constantA));
         System.out.println(" log(constantB)= "+Math.log(constantB));
         System.out.println("minC= "+minC);
         System.out.println(" currentC= "+currentC);


        //find the location of the minimum power of 10
        while ( currentC > minC )
          {
             minLocationC+=1;
             currentA=   minA - (minLocationC-minLocationA) /unitSpaceA;
             currentB=   minB - (minLocationC-minLocationB) /unitSpaceB;
             currentC= (double)(
                         exponentA*currentA+ 
Math.log(constantA)/Math.log(10)
                      + (double)exponentB*currentB+ 
Math.log(constantB)/Math.log(10)
                         )/exponentC;
          }

             //minLocationC -=10;

             currentA=   maxA;
             currentB=   maxB;
             currentC=   (double) (
                             exponentA*maxA+ 
Math.log(constantA)/Math.log(10)
                              + (double) exponentB*maxB+ 
Math.log(constantB)/Math.log(10)
                              )/exponentC;


         System.out.println("maxC= "+maxC);
         System.out.println(" currentC= "+currentC);

        //find the location of the maximum power of 10
        while ( currentC < maxC )
          {
             maxLocationC -=1;
             currentA=   maxA + (maxLocationA-maxLocationC)/unitSpaceA;
             currentB=   maxB + (maxLocationB-maxLocationC)/unitSpaceB;
             currentC=   (double) (
                          exponentA*currentA+ 
Math.log(constantA)/Math.log(10) +
                          (double) exponentB* currentB+ 
Math.log(constantB)/Math.log(10)
                          )/exponentC;
           }

      }


     if ( (exType==3) )
      {
        double currentA=   minA ;
        double currentB=   maxB ;
        double currentC;
               currentC= (double) exponentA*minA + 
Math.log(constantA)/Math.log(10) -
                         (double) exponentB*maxB - 
Math.log(constantB)/Math.log(10);

         System.out.println("minA= "+minA);
         System.out.println("maxA= "+maxA);
         System.out.println(" log(constantA)= "+Math.log(constantA));
         System.out.println(" log(constantB)= "+Math.log(constantB));
         System.out.println("minC= "+minC);
         System.out.println(" currentC= "+currentC);


        //find the location of the minimum power of 10
        while ( currentC > minC )
          {  //g.setColor(Color.green);
             //g.drawString("fukker",50,50);
             minLocationC+=1;
             currentA=   minA - (minLocationC-minLocationA) /unitSpaceA;
             currentB=   maxB + (minLocationC-minLocationB) /unitSpaceB;
             currentC= (double)exponentA*currentA + 
Math.log(constantA)/Math.log(10)
                      - (double)exponentB*currentB - 
Math.log(constantB)/Math.log(10);
          }

             //minLocationC -=10;

             currentA=   maxA;
             currentB=   minB;
             currentC=   (double) exponentA * maxA + 
Math.log(constantA)/Math.log(10)
                          - (double) exponentB * minB - 
Math.log(constantB)/Math.log(10);


         System.out.println("maxC= "+maxC);
         System.out.println(" currentC= "+currentC);

        //find the location of the maximum power of 10
        while ( currentC < maxC )
          {  //g.drawString("shit", 100,100);
             g.setColor(Color.black);
             maxLocationC -=1;
             currentA=   maxA + (maxLocationA-maxLocationC)/unitSpaceA;
             currentB=   minB - (maxLocationB-maxLocationC)/unitSpaceB;
             currentC=   (double) exponentA * currentA + 
Math.log(constantA)/Math.log(10)
                          - (double) exponentB * currentB - 
Math.log(constantB)/Math.log(10);
           }

      }


///////////////////////////////////////////////////////////////////////////
//draw the middle scale in its proper location

        g.drawLine(mid, maxLocationC-10, mid, minLocationC+10);
        g.drawLine(mid-1, maxLocationC-10, mid-1, minLocationC+10);
        g.drawLine(mid-10, maxLocationC-10, mid+10, maxLocationC-10);
        g.drawLine(mid-10, maxLocationC-9, mid+10, maxLocationC-9);
        g.drawLine(mid-10, minLocationC+10, mid+10, minLocationC+10);
        g.drawLine(mid-10, minLocationC+9, mid+10, minLocationC+9);
        g.setFont(boldFont);
        g.drawString(nameC, mid-10, minLocationC+25);
        g.setFont(normalFont);

       g.drawLine(mid-5, minLocationC, mid+5, minLocationC);



/////////////////////////////////////
//now label the min and max values
//on the middle scale

        if (logarithmic==1)
        {
        g.drawString("10", mid+7, minLocationC+10-6);
        g.setFont(smallFont);
        g.drawString(""+(int)minC, mid+20, minLocationC+10-12);
        g.setFont(normalFont);
        }
        else
        g.drawString(""+minC, mid+7, minLocationC+10-6);

        g.drawLine(mid-5, maxLocationC, mid+5, maxLocationC);

         if (logarithmic==1)
        {
        g.drawString("10", mid+7, maxLocationC-10+15);
        g.setFont(smallFont);
        g.drawString(""+(int)maxC, mid+20, maxLocationC-10+9);
        g.setFont(normalFont);
        }
        else
        g.drawString(""+maxC, mid+7, maxLocationC-10+15);




////////////////////////////////////////
//label the middle scale
//
        difference =  maxC- minC;
        locationDifference= minLocationC-maxLocationC;
        double unitSpaceC=  locationDifference/difference;


        if (unitSpaceC > 20)
            interval=1;
            else
              interval=  (int)  (20/unitSpaceC);

        if (intervalInputC==1)
        {
        Double intervalD= new Double(intervalC.getText());
        interval= intervalD.doubleValue();
        }



         minInterval= (int)minC;

         for ( i=minInterval+interval; i<=maxC; i+=interval)
            {
                intervalPosition= minLocationC - (i-minC)*unitSpaceC;
              if (intervalPosition> maxLocationC+10)
               {
                g.drawLine(mid-3, (int)(intervalPosition),
                           mid+3, (int)(intervalPosition));

                if (logarithmic==1)
                {
                 g.drawString("10", mid+5, (int) intervalPosition+5);
                 g.setFont(smallFont);
                 g.drawString(""+(int)i, mid+18, (int)intervalPosition-1);
                 g.setFont(normalFont);
                 }
                else
                g.drawString(""+(int)i, mid+5, (int)(intervalPosition+5));
               }
            }




////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
//if user is done drawing the line,
//calculate and output the values of the 3 variables
//in a solution box
///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
        //location of the solution box
        int solutionX=20;
        int solutionY=height-lineSpace+60;

        if (redline==1)
         {g.setFont(f);
          g.setColor(Color.blue);
          //make the border for the output
          g.drawRect(solutionX, solutionY, 250, 70);
          g.drawRect(solutionX+1, solutionY+1, 248, 68);
          g.setColor(Color.red);
          //figure out slope and y-intercept
          slope= ( (float) (ends.y-starts.y) /
                   (float) (ends.x-starts.x));
          B= (starts.y- slope*starts.x);
          //figures out the intersection points
          y1= (slope*lineSpace +B);
          y2= (slope*mid + B);
          y3= (slope*(width-lineSpace) + B);
          //y4= 550- (slope*323 + B);

          intercept1= new Point(lineSpace,  (int) y1);
          //g.drawString("1st intercept is " +  intercept1.x + ", "
          //              + intercept1.y , 400,40);
          intercept2= new Point(mid, (int) y2 );
          //g.drawString("2nd intercept is " +  intercept2.x + ", "
          //              + intercept2.y , 400,70);
          intercept3= new Point(width-lineSpace, (int) y3 );
          //g.drawString("3rd intercept is " +  intercept3.x + ", "
          //              + intercept3.y , 400,100);
          //intercept4= new Point(323, (int) +y4 );
          //g.drawString("4th intercept is " +  intercept4.x + ", "
          //              + intercept4.y , 400,130);



     double valueA, valueB, valueC;



      switch (exType) {
        case 1: //multiplication

        valueA= minA + (minLocationA-intercept1.y)/unitSpaceA;
        valueB= minB + (minLocationB-intercept3.y)/unitSpaceB;
        valueA= Math.pow(10, valueA);
        valueB= Math.pow(10, valueB);
        valueA= roundOff(valueA);
        valueB= roundOff(valueB);
        valueC= constantA*valueA  *  constantB*valueB;

        break;

        case 2: //addition
        valueA= minA + (minLocationA- intercept1.y)/unitSpaceA;
        valueB= minB + (minLocationB- intercept3.y)/unitSpaceB;
        valueA= roundOff(valueA);
        valueB= roundOff(valueB);
        valueC= constantA*valueA  +  constantB*valueB;

        break;

        case 3: //division
        valueA= minA + (minLocationA-intercept1.y)/unitSpaceA;
        valueB= maxB - (minLocationB-intercept3.y)/unitSpaceB;
        valueA= Math.pow(10,valueA);
        valueB= Math.pow(10,valueB);
        valueA= roundOff(valueA);
        valueB= roundOff(valueB);
        valueC= (constantA*valueA)  /  (constantB*valueB);

         break;

        case 4: //subtraction
        valueA= minA + (minLocationA-intercept1.y)/unitSpaceA;
        valueB= maxB - (minLocationB-intercept3.y)/unitSpaceB;
        valueA= roundOff(valueA);
        valueB= roundOff(valueB);
        valueC= constantA*valueA  -  constantB*valueB;
        break;

        default:

        case 5: //multiplication with exponents
        valueA= minA + (minLocationA-intercept1.y)/unitSpaceA;
        valueB= minB + (minLocationB-intercept3.y)/unitSpaceB;
        valueA= Math.pow(10,valueA);
        valueB= Math.pow(10,valueB);
        valueA= roundOff(valueA);
        valueB= roundOff(valueB);
        valueC= constantA * Math.pow(valueA,exponentA) *
                constantB * Math.pow(valueB,exponentB);
        break;

        }


          //output the values
          g.drawString(""+nameA+"= "+
                        valueA +" "+unitsA, solutionX+10, solutionY+20);
          g.setFont(f2);
          g.setColor(Color.black);
          g.setFont(f);
          g.setColor(Color.red);
          g.drawString(""+nameB+"= "+
                        valueB +" " +unitsB, solutionX+10, solutionY+40);
          g.setFont(f2);
          g.setColor(Color.black);
          g.setFont(f);
          g.setColor(Color.red);
          g.drawString(""+nameC+"= "+
                         roundOff(valueC) +" " +unitsC,solutionX+10, 
solutionY+60);


          g.setFont(f2);
          g.setColor(Color.black);
          //g.setFont(f);
          //g.setColor(Color.red);




         }//end of if redline

         else
           {g.setColor(Color.red);
            g.drawString(" "+unitsA, lineSpace-10, height-lineSpace+30);
            g.drawString(" "+unitsC, mid-10, minLocationC+40);
            g.drawString(" "+unitsB, width-lineSpace-10, 
height-lineSpace+30);
            g.setColor(Color.black);
            }






     }//end of if (done==1)
   g.setColor(Color.black);
   g.setFont(nameFont);
   g.drawString("Created by Jethro Berelson and Professor Thomas Jones", 
width/2, height+20);


  }//end of paint function



///////////////////////////////////////////////////////
//called when the mouse is clicked in the applet
//initializes the values for the line
///////////////////////////////////////////////////////
   public boolean mouseDown(Event evt, int x, int y) {
     starts= new Point(x,y);
     ends= new Point(x,y);
     redline=0;
     return true;
  }


////////////////////////////////////////////////////////
//called when mouse is released
////////////////////////////////////////////////////////
   public boolean mouseUp(Event evt, int x, int y) {
      ends= new Point(x,y);
      //when line is done being drawn, make it red
      redline=1;
      repaint();
      return true;
     }


///////////////////////////////////////////////////////////
//dynamically draws the line as mouse is being dragged
///////////////////////////////////////////////////////////
    public boolean mouseDrag(Event evt, int x, int y) {
           currentpoint=new Point(x,y);
           repaint();
          return true;}


///////////////////////////////////////////////////////
//changed update funtion to reduce flickering
//////////////////////////////////////////////////////
     public void update(Graphics g) {
       paint(g);
      }





///////////////////////////////////////////////////////////
//actionPerformed function- used for event handling
//////////////////////////////////////////////////////////

public void actionPerformed(ActionEvent e)
  {
    if   (e.getSource()== mult)
       {
         exType= 1;
         this.getUserInput();
          getExpression.dispose();
        }


    if   (e.getSource()== addition)
       {
         exType= 2;
         this.getUserInput();
          getExpression.dispose();
        }


    if   (e.getSource()== div)
       {
         exType= 3;
         this.getUserInput();
          getExpression.dispose();
        }


    if   (e.getSource()== sub)
       {
         exType= 4;
         this.getUserInput();
          getExpression.dispose();
        }


    if   (e.getSource()== five)
       {
         exType= 5;
         this.getUserInput();
          getExpression.dispose();
        }


    if (e.getSource()== okButton)
      {  this.storeData();
         getInput.dispose();
         repaint();
      }


    if (e.getSource()== okButton2)
      {
         this.storeData2();
         getInput2.dispose();
         done=1;
         repaint();
      }


    if (e.getSource()== restart)
       {  getInput.dispose();
          getInput2.dispose();
          getExpression.dispose();
          this.begin();
        }


    if ((e.getSource()==intervalA) || (e.getSource()==interval1Button))
        {intervalInputA=1;
         repaint();
         }

    if ((e.getSource()==intervalB) || (e.getSource()==interval2Button))
         {intervalInputB=1;
          repaint();
          }

    if ((e.getSource()==intervalC) || (e.getSource()==interval3Button))
         { intervalInputC=1;
           repaint();
          }

    if (e.getSource()==closeWindow)
       { restartFrame.dispose();
       }

   // if (e.getSource()==closeWindow)
    //  {getInput2.dispose();
    //  }

}
}



