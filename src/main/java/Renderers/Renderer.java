package Renderers;

import functions.ExponentialFunction;
import functions.LinearFunction;
import functions.LogaritmicFunction;
import functions.QuadraticFunction;
import global.AbstractRenderer;
import lwjglutils.OGLTextRenderer;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.*;
import transforms.Col;
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


    private ArrayList<Vec2D> points = new ArrayList<>();

    private static Map<Integer, Character> keyToCharMap = new HashMap<>();

    private final double X_MIN = -10.0;
    private final double X_MAX = 10.0;

    private final double Y_MIN = -10.0;
    private final double Y_MAX = 10.0;

    private final double Z_MIN = -10.0;
    private final double Z_MAX = 10.0;
    private final double STEP = 0.1;

    private boolean isConfirmed = false;


    private String text = "";


    public Renderer() {
        super();
       initKeys();


        glfwWindowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
                    textRenderer.resize(width, height);
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
                    System.out.println(xPos[0]);

                    clickedX = (float) (20 * xPos[0] / width - 10);
                    clickedY = (float) (10 - 20 * yPos[0] / height);

                    points.add(new Vec2D(clickedX, clickedY));

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

                        if( keyToCharMap.containsKey(i)) {
                            text += keyToCharMap.get(i);
                        }

                    }
                }
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
        glOrtho(X_MIN, X_MAX, Y_MIN, Y_MAX, Z_MIN, Z_MAX); // Upraveno rozsah osy y


        initGrid();
        //drawInputs();

        if (!points.isEmpty()) {
            drawPoints();
        }

        Pattern pattern = Pattern.compile(LinearFunction.regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches() && isConfirmed) {
            double a;

            if (matcher.group(1) == null || matcher.group(1).isEmpty()) {
                a = 1.0;
            } else {
                String value = (matcher.group(1).replace(" ", ""));
                if(matcher.group(1).toString().equals("-")) {
                    a = - 1.0;
                } else {
                    a = Double.parseDouble(value);
                }
            }

            double b;

            if (matcher.group(2) == null || matcher.group(2).isEmpty()) {
                b = 0;
            } else {
                b = Double.parseDouble(matcher.group(2).replace(" ", ""));
            }
            drawLinear(a, b);
        }

        Pattern patternQuadratic = Pattern.compile(QuadraticFunction.regex);
        Matcher matcherQuadratic = patternQuadratic.matcher(text);

        if(matcherQuadratic.matches() && isConfirmed) {
            double a;

            if (matcherQuadratic.group(1) == null || matcherQuadratic.group(1).isEmpty()) {
                a = 1.0;
            } else {
                String value = (matcherQuadratic.group(1).replace(" ", ""));
                if(matcherQuadratic.group(1).toString().equals("-")) {
                    a = - 1.0;
                } else {
                    a = Double.parseDouble(value);
                }
            }

            double b;

            if (matcherQuadratic.group(2) == null || matcherQuadratic.group(2).isEmpty()) {
                b = 0;
            } else {
                b = Double.parseDouble(matcherQuadratic.group(2).replace(" ", ""));
            }

            double c;

            if (matcherQuadratic.group(3) == null || matcherQuadratic.group(3).isEmpty()) {
                c = 0;
            } else {
                c = Double.parseDouble(matcherQuadratic.group(3).replace(" ", ""));
            }

            drawQuadratic(a, b, c);
        }

        Pattern patternExponencial = Pattern.compile(ExponentialFunction.regex);
        Matcher matcherExponencial = patternExponencial.matcher(text);

        if(matcherExponencial.matches() && isConfirmed) {
            double a;

            if (matcherExponencial.group(1) == null || matcherExponencial.group(1).isEmpty()) {
                a = 1.0;
            } else {
                String value = (matcherExponencial.group(1).replace(" ", ""));
                if(matcherExponencial.group(1).toString().equals("-")) {
                    a = - 1.0;
                } else {
                    a = Double.parseDouble(value);
                }
            }

            double b;

            if (matcherExponencial.group(2) == null || matcherExponencial.group(2).isEmpty()) {
                b = 0;
            } else {
                b = Double.parseDouble(matcherExponencial.group(2).replace(" ", ""));
            }

            drawExponencial(a, b);
        }

        Pattern patternLogaritmic = Pattern.compile(LogaritmicFunction.regex);
        Matcher matcherLogaritmic = patternLogaritmic.matcher(text);

        if(matcherLogaritmic.matches() && isConfirmed) {

            double a;

            if (matcherLogaritmic.group(1) == null || matcherLogaritmic.group(1).isEmpty()) {
                a = 1.0;
            } else {
                String value = (matcherLogaritmic.group(1).replace(" ", ""));
                if (matcherLogaritmic.group(1).toString().equals("-")) {
                    a = -1.0;
                } else {
                    a = Double.parseDouble(value);
                }
            }

            double b;

            if (matcherLogaritmic.group(2) == null || matcherLogaritmic.group(2).isEmpty()) {
                b = 0;
            } else {
                b = Double.parseDouble(matcherLogaritmic.group(2).replace(" ", ""));
            }

            drawLogaritmic(a, b);
        }


        textRenderer.addStr2D(100, 80, text);
        textRenderer.draw();



        textRenderer.setBackgroundColor(Color.black);
        textRenderer.setColor(Color.white);
       // textRenderer.addStr2D(width / 2 + 5, height / 2 + 20, "0");

        int start = 0;

        for(double x = X_MIN; x <= X_MAX; x++) {
            textRenderer.addStr2D(start , height / 2 + 20, Integer.toString((int) x));
            start += width / 20;
        }

        start = 0;

        for(double y = Y_MAX; y >= Y_MIN; y--) {
            if(y == 0) {
                continue;
            }
            textRenderer.addStr2D(width / 2 + 10 , start, Integer.toString((int) y));
            start += height / 19;
        }



    }

    private void initGrid() {
        glBegin(GL_LINES);


        glColor3f(0.2f, 0.2f, 0.2f);

        // vodorovné čáry mřížky
        for (float i = (float) (Y_MIN * scale); i <= Y_MAX * scale; i += 1f * scale) {
            glVertex2f((float) (X_MIN * scale), i);
            glVertex2f((float) (X_MAX * scale), i);
        }

        // svislé čáry mřížky
        for (float i = (float) (X_MIN * scale); i <= X_MAX * scale; i += 1f * scale) {
            glVertex2f(i, (float) (Y_MIN * scale));
            glVertex2f(i, (float) (Y_MAX * scale));
        }

        // osa Y a X
        glColor3f(1f, 1f, 1f);
        glVertex2f((float) (X_MIN * scale), 0);
        glVertex2f((float) (X_MAX * scale), 0);

        glVertex2f(0, (float) (Y_MIN * scale));
        glVertex2f(0, (float) (Y_MAX * scale));

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
        glVertex2f(-7f, 7f);
        glVertex2f(-3f, 7f);
        glVertex2f(-3f, 6f);
        glVertex2f(-7f, 6f);
        glEnd();

    }

    private void initKeys()
    {
        keyToCharMap.put(GLFW_KEY_EQUAL, '=');
        keyToCharMap.put(GLFW_KEY_MINUS, '-');
        keyToCharMap.put(GLFW_KEY_KP_ADD, '+');
        keyToCharMap.put(GLFW_KEY_KP_SUBTRACT, '-');
        keyToCharMap.put(GLFW_KEY_KP_MULTIPLY, '*');
        keyToCharMap.put(GLFW_KEY_PERIOD, '.');
        keyToCharMap.put(GLFW_KEY_GRAVE_ACCENT, '^');
        keyToCharMap.put(GLFW_KEY_LEFT_BRACKET, '(');
        keyToCharMap.put(GLFW_KEY_RIGHT_BRACKET, ')');
        keyToCharMap.put(GLFW_KEY_SLASH, '_');

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


    }

    private void drawQuadratic(double a, double  b, double c) {
        QuadraticFunction quadraticFunction = new QuadraticFunction(a, b, c);

        glColor3f(1.0f, 0.0f, 0.0f); // Barva grafu
        glBegin(GL_LINE_STRIP);

        for (double x = X_MIN; x <= X_MAX; x += 0.3d) {
            double y = quadraticFunction.value(x);
            glVertex2d(x, y);
        }
        glEnd();
    }

    private void drawLinear(double a, double b) {

        LinearFunction linearFunction = new LinearFunction(a, b);

        glColor3f(1.0f, 0.0f, 0.0f); // Barva grafu
        glBegin(GL_LINE_STRIP);

        for (double x = X_MIN; x <= X_MAX; x += 1d) {
            double y = linearFunction.value(x);
            glVertex2d(x, y);
        }
        glEnd();
    }

    private void drawExponencial(double a, double b) {
        ExponentialFunction exponentialFunction = new ExponentialFunction(a, b);

        glColor3f(1.0f, 0.0f, 0.0f); // Barva grafu
        glBegin(GL_LINE_STRIP);

        for (double x = X_MIN; x <= X_MAX; x += 0.1d) {
            double y = exponentialFunction.value(x);
            glVertex2d(x, y);
        }
        glEnd();

    }

    private void drawLogaritmic(double a, double b) {
        LogaritmicFunction logarithmicFunction = new LogaritmicFunction(a, b);

        glColor3f(1.0f, 0.0f, 0.0f); // Barva grafu
        glBegin(GL_LINE_STRIP);

        for (double x = X_MIN; x <= X_MAX; x += 0.1d) {

            if (x > 0) { // Kontrola, zda je x kladné
                double y = logarithmicFunction.value(x);

                glVertex2d(x, y);
            }
        }
        glEnd();

    }

}
