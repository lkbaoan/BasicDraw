/** *************************************************************
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
 *************************************************************** */
package basicdraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class BasicDraw {

    private void renderEllipse(int xCenter, int yCenter, int rx, int ry) {
        glColor3f(0, GL_GREEN, 0);
        int rx2 = rx * rx;
        int ry2 = ry * ry;
        int twoRx2 = 2 * rx2;
        int twoRy2 = 2 * ry2;
        int p;
        int x = 0;
        int y = ry;
        int px = 0;
        int py = twoRx2 * y;

        drawEllipse(xCenter, yCenter, x, y);

        // Region 1
        p = (int) (ry2 - (rx2 * ry) + (0.25 * rx2));
        while (px < py) {
            x++;
            px += twoRy2;
            if (p < 0) {
                p += ry2 + px;
            } else {
                y--;
                py -= twoRx2;
                p += ry2 + px - py;
            }
            drawEllipse(xCenter, yCenter, x, y);
        }

        // Region 2
        p = (int) (ry2 * (x + 0.5) * (x + 0.5) + rx2 * (y - 1) * (y - 1) - rx2 * ry2);
        while (y > 0) {
            y--;
            py -= twoRx2;
            if (p > 0) {
                p += rx2 - py;
            } else {
                x++;
                px += twoRy2;
                p += rx2 - py + px;
            }
            drawEllipse(xCenter, yCenter, x, y);
        }

    }

    private void drawEllipse(int xCenter, int yCenter, int x, int y) {
        glBegin(GL_POINTS);
        glVertex2f(xCenter + x, yCenter + y);
        glVertex2f(xCenter - x, yCenter + y);
        glVertex2f(xCenter + x, yCenter - y);
        glVertex2f(xCenter - x, yCenter - y);
        glEnd();
    }

    private void renderCircle(int xCenter, int yCenter, int radius) {
        glColor3f(0, 0, GL_BLUE);

        int x = 0;
        int y = radius;
        int p = 1 - radius;

        drawCircle(xCenter, yCenter, x, y);
        while (x < y) {
            x++;
            if (p < 0) {
                p += 2 * x + 1;
            } else {
                y--;
                p += 2 * x - 2 * y + 1;
            }
            drawCircle(xCenter, yCenter, x, y);

        }

    }

    private void drawCircle(int xCenter, int yCenter, int x, int y) {
        glBegin(GL_POINTS);
        glVertex2f(xCenter + x, yCenter + y);
        glVertex2f(xCenter - x, yCenter + y);
        glVertex2f(xCenter + x, yCenter - y);
        glVertex2f(xCenter - x, yCenter - y);
        glVertex2f(xCenter + y, yCenter + x);
        glVertex2f(xCenter - y, yCenter + x);
        glVertex2f(xCenter + y, yCenter - x);
        glVertex2f(xCenter - y, yCenter - x);
        glEnd();
    }

    private void renderLine(int xStart, int yStart, int xEnd, int yEnd) {
        int dx = Math.abs(xEnd - xStart);
        int dy = Math.abs(yEnd - yStart);
        int x, y, d;
        d = 2 * dy - dx;
        if (xStart > xEnd) {
            x = xEnd;
            y = yEnd;
            xEnd = xStart;
        } else {
            x = xStart;
            y = yStart;
        }
        int incrementRight = 2 * dy;
        int incrementUpRight = 2 * dy - dx;
        boolean isUp = yEnd > yStart;

        glColor3f(GL_RED, 0, 0);

        while (x < xEnd) {
            glBegin(GL_POINTS);
            glVertex2f(x, y);
            glEnd();

            x++;
            if (d <= 0) {
                d += incrementRight;
            } else {
                if (isUp) {
                    y++;
                } else {
                    y--;
                }
                d += incrementUpRight;
            }
        }

    }

    // method: start
    // purpose: start a new window and render graphics
    public void start(ArrayList<String> toRender) {
        try {
            createWindow();
            initGL();
            render(toRender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method: createWindow
    // purpose: create a new window display with set size and title
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640, 480));
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

    private void toRenderLine(String str) {
        String[] line = str.trim().split("\\s+");
        String[] c1 = line[1].split(",");
        String[] c2 = line[2].split(",");
        int[] coordinate = {
            Integer.parseInt(c1[0]),
            Integer.parseInt(c1[1]),
            Integer.parseInt(c2[0]),
            Integer.parseInt(c2[1])
        };
        System.out.println(coordinate[0] + " | " + coordinate[1] + " | " + coordinate[2] + " | " + coordinate[3]);
        renderLine(coordinate[0], coordinate[1], coordinate[2], coordinate[3]);
    }

    private void toRenderCircle(String str) {
        String[] line = str.trim().split("\\s+");
        String[] c1 = line[1].split(",");
        String[] c2 = line[2].split(",");
        int[] coordinate = {
            Integer.parseInt(c1[0]),
            Integer.parseInt(c1[1]),
            Integer.parseInt(c2[0]),};
        System.out.println(coordinate[0] + " | " + coordinate[1] + " | " + coordinate[2]);
        renderCircle(coordinate[0], coordinate[1], coordinate[2]);
    }

    private void toRenderEllipse(String str) {
        String[] line = str.trim().split("\\s+");
        String[] c1 = line[1].split(",");
        String[] c2 = line[2].split(",");
        int[] coordinate = {
            Integer.parseInt(c1[0]),
            Integer.parseInt(c1[1]),
            Integer.parseInt(c2[0]),
            Integer.parseInt(c2[1])
        };
        System.out.println(coordinate[0] + " | " + coordinate[1] + " | " + coordinate[2] + " | " + coordinate[3]);
        renderEllipse(coordinate[0], coordinate[1], coordinate[2], coordinate[3]);
    }

    // method: render
    // purpose: render graphics based on command
    private void render(ArrayList<String> command) {
        Iterator it = command.iterator();
        while (!Display.isCloseRequested()) {
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();

                glColor3f(1.0f, 1.0f, 0.0f);
                glPointSize(1);

                glBegin(GL_POINTS);
                renderLine(10, 380, 380, 10);
                renderCircle(320, 100, 54);
                renderEllipse(100, 100, 45, 80);
                renderLine(350, 50, 500, 70);
                renderCircle(50, 50, 100);
                renderEllipse(450, 250, 75, 35);
//                while (it.hasNext()) {
//                    String str = it.next().toString();
//                    switch (str.charAt(0)) {
//                        case 'l':
//                            toRenderLine(str);
//                            break;
//                        case 'c':
//                            toRenderCircle(str);
//                            break;
//                        case 'e':
//                            toRenderEllipse(str);
//                            break;
//                        default:
//                            throw new AssertionError();
//                    }
//                }
//                while (it.hasNext()) {
//                    String line = it.next().toString();
//                    String[] spaceSplit = line.trim().split("\\s+");
//                    String[] coordinate1 = spaceSplit[1].split(",");
//                    String[] coordinate2 = spaceSplit[2].split(",");
//                    System.out.println("Splitted " + spaceSplit[0] + " | " + spaceSplit[1] + " | " + spaceSplit[2]);
//                    int[] commandSplit;
//                    commandSplit = new int[4];
//                    commandSplit[0] = Integer.parseInt(coordinate1[0]);
//                    commandSplit[1] = Integer.parseInt(coordinate1[1]);
//                    commandSplit[2] = Integer.parseInt(coordinate2[0]);
//
//                    switch (spaceSplit[0]) {
//                        case "c" -> {
//                            System.out.println(commandSplit[0] + " | " + commandSplit[1] + " | " + commandSplit[2]);
//                            renderCircle(commandSplit[0], commandSplit[1], commandSplit[2]);
//                        }
//                        case "l" -> {
//                            commandSplit[3] = Integer.parseInt(coordinate2[1]);
//                            System.out.println(commandSplit[0] + " | " + commandSplit[1] + " | " + commandSplit[2] + " | " + commandSplit[3]);
//                            renderLine(commandSplit[0], commandSplit[1], commandSplit[2], commandSplit[3]);
//                        }
//                        case "e" -> {
//                            commandSplit[3] = Integer.parseInt(coordinate2[1]);
//                            System.out.println(commandSplit[0] + " | " + commandSplit[1] + " | " + commandSplit[2] + " | " + commandSplit[3]);
//                            renderEllipse(commandSplit[0], commandSplit[1], commandSplit[2], commandSplit[3]);
//                        }
//                    }
//                }
                glEnd();
                Display.update();
                Display.sync(60);
            } catch (Exception e) {
            }
        }
        Display.destroy();
    }

    // method: main
    // purpose: start the program
    public static void main(String[] args) {
        BasicDraw program = new BasicDraw();

        ArrayList<String> str = new ArrayList<>();

        try {
            Scanner read = new Scanner(new File(args[0]));
            while (read.hasNextLine()) {
                String data = read.nextLine();
                str.add(data);
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        program.start(str);
    }

}
