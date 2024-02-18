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
    public void start(int[][] line, int[][] circle, int[][] ellipse) {
        try {
            createWindow();
            initGL();
            render(line, circle, ellipse);
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
    private void render(int[][] line, int[][] circle, int[][] ellipse) {
        System.out.printf("%d | %d | %d\n", line.length, circle.length, ellipse.length);
//        for (int i = 0; i < 2; i++) {
//            System.out.printf("%d, %d, %d, %d | %d, %d, %d | %d, %d, %d, %d\n", line[i][0], line[i][1], line[i][2], line[i][3], circle[i][0], circle[i][1], circle[i][2], ellipse[i][0], ellipse[i][1], ellipse[i][2], ellipse[i][3]);
//        }
//
//        int[][] line = {
//            {10, 380, 380, 10},
//            {350, 50, 500, 70}
//        };
//        int[][] circle = {
//            {320, 100, 54},
//            {50, 50, 100}
//        };
//        int[][] ellipse = {
//            {100, 100, 45, 80},
//            {450, 250, 75, 35}
//        };
        while (!Display.isCloseRequested()) {
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();

                glColor3f(1.0f, 1.0f, 0.0f);
                glPointSize(1);

                glBegin(GL_POINTS);
                for (int[] l : line) {
                    renderLine(l[0], l[1], l[2], l[3]);
                }
                for (int[] l : circle) {
                    renderCircle(l[0], l[1], l[2]);
                }
                for (int[] l : ellipse) {
                    renderEllipse(l[0], l[1], l[2], l[3]);
                }
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
        int[] counter = {0, 0, 0};
        int[][] line, circle, ellipse;
        int[] ite = {0, 0, 0};
        try {
            Scanner read = new Scanner(new File(args[0]));
            while (read.hasNextLine()) {
                String data = read.nextLine();
                str.add(data);
                switch (data.charAt(0)) {
                    case 'l' ->
                        counter[0]++;
                    case 'c' ->
                        counter[1]++;
                    case 'e' ->
                        counter[2]++;
                    default ->
                        throw new AssertionError();
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        line = new int[counter[0]][4];
        circle = new int[counter[1]][3];
        ellipse = new int[counter[2]][4];
        for (String s : str) {
            String[] separate = s.trim().split("\\s+");
            String[] c1 = separate[1].split(",");
            String[] c2 = separate[2].split(",");

            switch (separate[0]) {
                case "l" -> {
                    line[ite[0]][0] = Integer.parseInt(c1[0]);
                    line[ite[0]][1] = Integer.parseInt(c1[1]);
                    line[ite[0]][2] = Integer.parseInt(c2[0]);
                    line[ite[0]][3] = Integer.parseInt(c2[1]);
                    ite[0]++;
                }
                case "c" -> {
                    circle[ite[1]][0] = Integer.parseInt(c1[0]);
                    circle[ite[1]][1] = Integer.parseInt(c1[1]);
                    circle[ite[1]][2] = Integer.parseInt(c2[0]);
                    ite[1]++;
                }
                case "e" -> {
                    ellipse[ite[2]][0] = Integer.parseInt(c1[0]);
                    ellipse[ite[2]][1] = Integer.parseInt(c1[1]);
                    ellipse[ite[2]][2] = Integer.parseInt(c2[0]);
                    ellipse[ite[2]][3] = Integer.parseInt(c2[1]);
                    ite[2]++;
                }
                default ->
                    throw new AssertionError();
            }
        }

        program.start(line, circle, ellipse);
    }

}
