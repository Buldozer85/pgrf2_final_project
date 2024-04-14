package Renderers;

import functions.LinearFunction;
import global.AbstractRenderer;
import lwjglutils.OGLTextRenderer;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.*;
import transforms.Vec2D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * Simple scene rendering
 *
 * @author PGRF FIM UHK
 * @version 3.1
 * @since 2020-01-20
 */
public class Renderer extends AbstractRenderer {

    private float scale = 1.0f;
    private float clickedX = -1;
    private float clickedY = -1;

    private OGLTextRenderer textRenderer;

    private ArrayList<Vec2D> points = new ArrayList<>();

    private static Map<Integer, Character> keyToCharMap = new HashMap<>();

    private final double X_MIN = -1.0;
    private final double X_MAX = 1.0;
    private final double STEP = 0.1;

    private boolean isConfirmed = false;


    private String text = "";

    public Renderer() {
        super();

        keyToCharMap.put(GLFW_KEY_EQUAL, '=');
        keyToCharMap.put(GLFW_KEY_MINUS, '-');
        keyToCharMap.put(GLFW_KEY_KP_ADD, '+');
        keyToCharMap.put(GLFW_KEY_KP_SUBTRACT, '-');
        keyToCharMap.put(GLFW_KEY_KP_MULTIPLY, '*');

        // Přidat čísla
        keyToCharMap.put(GLFW_KEY_0, '0');
        keyToCharMap.put(GLFW_KEY_1, '1');
        keyToCharMap.put(GLFW_KEY_2, '2');
        keyToCharMap.put(GLFW_KEY_3, '3');
        keyToCharMap.put(GLFW_KEY_4, '4');
        keyToCharMap.put(GLFW_KEY_5, '5');
        keyToCharMap.put(GLFW_KEY_6, '6');
        keyToCharMap.put(GLFW_KEY_7, '7');
        keyToCharMap.put(GLFW_KEY_8, '8');
        keyToCharMap.put(GLFW_KEY_9, '9');
        keyToCharMap.put(GLFW_KEY_KP_0, '0');
        keyToCharMap.put(GLFW_KEY_KP_1, '1');
        keyToCharMap.put(GLFW_KEY_KP_2, '2');
        keyToCharMap.put(GLFW_KEY_KP_3, '3');
        keyToCharMap.put(GLFW_KEY_KP_4, '4');
        keyToCharMap.put(GLFW_KEY_KP_5, '5');
        keyToCharMap.put(GLFW_KEY_KP_6, '6');
        keyToCharMap.put(GLFW_KEY_KP_7, '7');
        keyToCharMap.put(GLFW_KEY_KP_8, '8');
        keyToCharMap.put(GLFW_KEY_KP_9, '9');

        glfwWindowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
            }
        };

        /*used default glfwKeyCallback */

        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long l, int i, int i1, int i2) {
                if (i == GLFW_MOUSE_BUTTON_LEFT && i1 == GLFW_PRESS) {
                    double[] xPos = new double[1];
                    double[] yPos = new double[1];
                    glfwGetCursorPos(l, xPos, yPos);

                    clickedX = (float) (2 * xPos[0] / width - 1);
                    clickedY = (float) (1 - 2 * yPos[0] / height);

                    points.add(new Vec2D(clickedX, clickedY));

                    System.out.println("Mouse clicked at: (" + clickedX + ", " + clickedY + ")");

                }
            }
        }; //glfwMouseButtonCallback do nothing


        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                scale += dy * 0.1f;
            }
        };

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                //
            }
        };

        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long l, int i, int i1, int i2, int i3) {
                // Pokud byla stisknuta klávesa a akce je stisknutí
                if (i2 == GLFW_PRESS) {
                    // Přidání znaku do textového řetězce na základě stisknuté klávesy
                    if (i == GLFW_KEY_BACKSPACE && text.length() > 0) {
                        isConfirmed = false;
                        text = text.substring(0, text.length() - 1); // Smazání posledního znaku
                    } else if (i >= GLFW_KEY_A && i <= GLFW_KEY_Z) {
                        char character = (char) ('a' + (i - GLFW_KEY_A)); // Převod kódu klávesy na znak
                        text += character;
                    } else if (i == GLFW_KEY_ENTER) {
                        isConfirmed = true;

                    } else {
                        text += keyToCharMap.get(i);
                    }
                }

                System.out.println(text);
            }
        };
    }

    @Override
    public void init() {
        textRenderer = new OGLTextRenderer(width, height);
        textRenderer.setBackgroundColor(Color.WHITE);
        textRenderer.setColor(Color.BLACK);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void display() {
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        // Nastavení projekční matice
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-1, 1, -1, 1, 1, -1); // Upraveno rozsah osy y


        initGrid();

        if (!points.isEmpty()) {
            drawPoints();
        }
//gm
        final String regexLinear = "^\\s*y\\s*=\\s*([+-]?\\s*\\d*\\.?\\d*)\\s*\\*?\\s*x\\s*([+-]?\\s*\\d+\\.?\\d*)?\\s*$";

        Pattern pattern = Pattern.compile(regexLinear);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches() && isConfirmed) {
            double a;

            if (matcher.group(1).isEmpty()) {
                a = 1.0;
            } else {
                a = Double.parseDouble(matcher.group(1).replace(" ", ""));
            }

            double b;

            if (matcher.group(1).isEmpty()) {
                b = 0;
            } else {
                b = Double.parseDouble(matcher.group(2).replace(" ", ""));
            }

            LinearFunction linearFunction = new LinearFunction(a, b);


            // Vykreslení grafu funkce
            glColor3f(1.0f, 0.0f, 0.0f); // Barva grafu
            glBegin(GL_LINE_STRIP);
            double maxY = Double.NEGATIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;

// Vypočítání maximální a minimální hodnoty y
/*
            for (double x = X_MIN; x <= X_MAX; x += STEP) {
                double y = linearFunction.value(x);
                if (y > maxY) {
                    maxY = y;
                }
                if (y < minY) {
                    minY = y;
                }
            }
*/

// Normalizace hodnot y do rozsahu [-1, 1]
            //double yRange = maxY - minY;
            for (double x = -1; x <= 1d; x += 0.1d) {
                double y = linearFunction.value(x); // Normalizace do rozsahu [-1, 1]
                System.out.println(x + "x y:" + y);
                glVertex2d(x, y);
            }
            glEnd();
        }


        textRenderer.addStr2D(100, 80, text);
        textRenderer.draw();
    }

    private void initGrid() {
        glBegin(GL_LINES);


        glColor3f(0.2f, 0.2f, 0.2f);

        // vodorovné čáry mřížky
        for (float i = -1.0f * scale; i <= 1.0f * scale; i += 0.1f * scale) {
            glVertex2f(-1.0f * scale, i);
            glVertex2f(1.0f * scale, i);
        }

        // svislé čáry mřížky
        for (float i = -1.0f * scale; i <= 1.0f * scale; i += 0.1f * scale) {
            glVertex2f(i, -1.0f * scale);
            glVertex2f(i, 1.0f * scale);
        }

        // osa Y a X
        glColor3f(1f, 1f, 1f);
        glVertex2f(-1 * scale, 0);
        glVertex2f(1 * scale, 0);

        glVertex2f(0, 1 * scale);
        glVertex2f(0, -1 * scale);

        // glColor3f(0.5f, 0.5f, 0.5f);

        glEnd();
    }

    private void drawPoints() {
        glBegin(GL_POINTS);
        glColor3f(1f, 0, 0);

        for (Vec2D point : points) {
            glVertex2f((float) point.getX(), (float) point.getY());
        }


        glEnd();
    }

    private void drawInputs() {

        glBegin(GL_QUADS);
        glColor3f(1f, 1f, 1f);
        glVertex2f(-0.7f, 0.7f);
        glVertex2f(-0.3f, 0.7f);
        glVertex2f(-0.3f, 0.6f);
        glVertex2f(-0.7f, 0.6f);
        glEnd();

    }

    private Vec2D normalize(Vec2D v) {
        return new Vec2D((2 * v.getX() / width - 1), (1 - 2 * v.getY() / height));
    }

}
