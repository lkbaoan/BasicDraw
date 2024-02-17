/***************************************************************
* file: BasicDraw.java
* author: A. Le
* class: CS 4450 – Computer Graphics
*
* assignment: program 1
* date last modified: 02/07/2024
*
* purpose: This program accept a file as an argument as the input then draw line, shape, 
* color based on the value from the file.
*
****************************************************************/
package basicdraw;

import java.io.File;
import java.util.Scanner;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class BasicDraw {
    private void renderLine(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int d = dy - (dx/2);
        int incrementRight = 2*dy;
        int incrementUpRight = 2*(dy-dx);
        int x,y;
        
        if (x1 > x2) {
            x = x2;
            y = y2;
            x2 = x1;
        } else {
            x = x1;
            y = y1;
        }

        while (!Display.isCloseRequested()) {
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();

                glColor3f(GL_RED, 0, 0);
                glPointSize(10);

                glBegin(GL_POINTS);
                    glVertex2i(x,y);
                glEnd();                
                while (x < x2) {
                    x++;
                    if (d < 0) {
                        d+=incrementRight;
                    } else {
                        d+=incrementUpRight;
                        y++;
                    }
                    glBegin(GL_POINTS);
                    glVertex2i(x,y);
                    glEnd();
                }

                Display.update();
                Display.sync(60);
            } catch (Exception e) {}

        }
        
    }
    // method: start
    // purpose: start a new window and render graphics
    public void start() {
        try {
            createWindow();
            initGL();
            renderLine(10,380,380,10);
//            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // method: createWindow
    // purpose: create a new window display with set size and title
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640,480));
        Display.setTitle("Program 1");
        Display.create();
    }
    
    // method: initGL
    // purpose: initilize openGL task
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        glOrtho(0, 640, 0, 480, 1, -1);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    // method: render
    // purpose: render graphics based on command
    private void render() {
        while(!Display.isCloseRequested()) {
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                
                glColor3f(1.0f, 1.0f, 0.0f);
                glPointSize(1);
                
                glBegin(GL_POINTS);
//                    glVertex2f(350.0f, 150.0f);
//                    glVertex2f(50.0f, 50.0f);
//                    for (int i = 10; i < 90; i ++) {
//                        glVertex2f(i,i);
//                    }
                glEnd();
                Display.update();
                Display.sync(60);
            } catch (Exception e) {}    
        }
        Display.destroy();
    }
    
    // method: main
    // purpose: start the program
    public static void main(String[] args) {
        BasicDraw program = new BasicDraw();
        program.start();
    }
    
}
