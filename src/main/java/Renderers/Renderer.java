package Renderers;

import functions.*;
import global.AbstractRenderer;
import lwjglutils.OGLTextRenderer;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.*;
import services.RegexService;
import transforms.Vec2D;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * Simple scene rendering
 *
 * @author PGRF FIM UHK, Tomáš Dvořák
 * @version 3.1
 * @since 2020-01-20
 */
public class Renderer extends AbstractRenderer {

    private float clickedX = -1;
    private float clickedY = -1;

    private ArrayList<Vec2D> points = new ArrayList<>();

    private final double X_MIN = -10.0;

    private final double X_MAX = 10.0;

    private final double Y_MIN = -10.0;

    private final double Y_MAX = 10.0;

    private final double Z_MIN = -10.0;

    private final double Z_MAX = 10.0;

    private boolean isConfirmed = false;

    private String text = "";

    public Renderer() {
        super();

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

                    clickedX = (float) (20 * xPos[0] / width - 10);
                    clickedY = (float) (10 - 20 * yPos[0] / height);

                    points.add(new Vec2D(clickedX, clickedY));
                }
            }
        };

        glfwScrollCallback = null;

        glfwCursorPosCallback = null;

        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long l, int i, int i1, int i2, int i3) {
                // Pokud byla stisknuta klávesa a akce je stisknutí
                if (i2 == GLFW_PRESS) {
                    // Přidání znaku do textového řetězce na základě stisknuté klávesy
                    if (i == GLFW_KEY_BACKSPACE && text.length() > 0) {
                        isConfirmed = false;
                        text = text.substring(0, text.length() - 1); // Smazání posledního znaku
                    } else if (i == GLFW_KEY_ENTER) {
                        isConfirmed = true;

                    }
                }
            }
        };


        glfwCharCallback = new GLFWCharCallback() {

            @Override
            public void invoke(long l, int i) {
                text += Character.toString((char) i);
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
        glPointSize(5);
        initGrid();

        if (!points.isEmpty()) {
            drawPoints();
        }

        Pattern pattern = Pattern.compile(LinearFunction.regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches() && isConfirmed) {
            double a = Double.parseDouble(RegexService.extractGroupValue(matcher, LinearFunction.SLOPE, false));
            double b = Double.parseDouble(RegexService.extractGroupValue(matcher, LinearFunction.INTERCEPT, true));
            drawLinear(a, b);
        }

        Pattern patternQuadratic = Pattern.compile(QuadraticFunction.regex);
        Matcher matcherQuadratic = patternQuadratic.matcher(text);

        if(matcherQuadratic.matches() && isConfirmed) {
            double a = Double.parseDouble(RegexService.extractGroupValue(matcherQuadratic, QuadraticFunction.QUADRATIC_COEFFICIENT, false));
            double b = Double.parseDouble(RegexService.extractGroupValue(matcherQuadratic, QuadraticFunction.LINEAR_COEFFICIENT, true));
            double c = Double.parseDouble(RegexService.extractGroupValue(matcherQuadratic, QuadraticFunction.CONSTANT_COEFFICIENT, true));

            drawQuadratic(a, b, c);
        }

        Pattern patternExponencial = Pattern.compile(ExponentialFunction.regex);
        Matcher matcherExponencial = patternExponencial.matcher(text);

        if(matcherExponencial.matches() && isConfirmed) {
            double a = Double.parseDouble(RegexService.extractGroupValue(matcherExponencial, ExponentialFunction.AMPLITUDE, false));
            double b = Double.parseDouble(RegexService.extractGroupValue(matcherExponencial, ExponentialFunction.EXPONENT, true));
            drawExponencial(a, b);
        }

        Pattern patternLogaritmic = Pattern.compile(LogaritmicFunction.regex);
        Matcher matcherLogaritmic = patternLogaritmic.matcher(text);

        if(matcherLogaritmic.matches() && isConfirmed) {
            double a = Double.parseDouble(RegexService.extractGroupValue(matcherLogaritmic, LogaritmicFunction.AMPLITUDE, false));
            double b = Double.parseDouble(RegexService.extractGroupValue(matcherLogaritmic, LogaritmicFunction.BASE, true));
            drawLogaritmic(a, b);
        }

        Pattern patternTrigonometric = Pattern.compile(TrigonometricFunction.regex);
        Matcher matcherTrigonometric = patternTrigonometric.matcher(text);

        if(matcherTrigonometric.matches() && isConfirmed) {
            double a = Double.parseDouble(RegexService.extractGroupValue(matcherTrigonometric, TrigonometricFunction.AMPLITUDE, false));
            double f = Double.parseDouble(RegexService.extractGroupValue(matcherTrigonometric, TrigonometricFunction.FREQUENCY, false));
            double p = Double.parseDouble(RegexService.extractGroupValue(matcherTrigonometric, TrigonometricFunction.PHASE_SHIFT, true));
            double v = Double.parseDouble(RegexService.extractGroupValue(matcherTrigonometric, TrigonometricFunction.VERTICAL_SHIFT, true));

            String type = RegexService.extractGroupValue(matcherTrigonometric, TrigonometricFunction.TYPE, false);

            drawTrigonometric(a, f, p, v, type);
        } else if (isConfirmed) {
          drawAbsolute();
        }
        textRenderer.addStr2D(10, 180, "Začněte psát funkci ve tvaru: y=\n");
        textRenderer.addStr2D(10, 210, "Potvrdíte stisknutím enter");
        textRenderer.addStr2D(10, 240, "Funkce:");
        textRenderer.addStr2D(60, 240, text);

        textRenderer.addStr2D(10, 20, "Logaritmická funkce: y=log_a(x)\n");
        textRenderer.addStr2D(10, 50,  "Kvadratická funkce: y=x^2...\n");
        textRenderer.addStr2D(10, 80,    "Exponenciální funkce y=e^(2x)");
        initNumbers();

    }

    private void initGrid() {
        glBegin(GL_LINES);

        glColor3f(0.2f, 0.2f, 0.2f);

        // vodorovné čáry mřížky
        for (double i = Y_MIN; i <= Y_MAX; i += 1d) {
            glVertex2d(X_MIN, i);
            glVertex2d(X_MAX, i);
        }

        // svislé čáry mřížky
        for (double i = X_MIN; i <= X_MAX; i += 1d) {
            glVertex2d(i, Y_MIN);
            glVertex2d(i, Y_MAX);
        }

        // osa Y a X
        glColor3f(1f, 1f, 1f);
        glVertex2d( X_MIN, 0);
        glVertex2d(X_MAX, 0);

        glVertex2d(0,Y_MIN);
        glVertex2d(0,Y_MAX);

        glEnd();
    }

    private void drawPoints() {
        glBegin(GL_POINTS);
        glColor3f(1f, 0, 0);

        for (Vec2D point : points) {
            glVertex2d(point.getX(), point.getY());
        }

        glEnd();
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

    private void drawTrigonometric(double a, double f, double p, double v, String type) {
        TrigonometricFunction trigonometricFunction = new TrigonometricFunction(a, f, p, v, type);

        glColor3f(1.0f, 0.0f, 0.0f); // Barva grafu
        glBegin(GL_LINE_STRIP);

        double lastY = trigonometricFunction.value(X_MIN); // Počáteční hodnota Y pro první bod
        boolean isFirstPoint = true; // Proměnná pro kontrolu, jestli se jedná o první bod nové periody

        for (double x = X_MIN; x <= X_MAX; x += 0.1d) {
            double y = trigonometricFunction.value(x);

            // Pokud se přesunete do nové periody, začněte novou úsečku
            if (y * lastY < 0 && !isFirstPoint) {
                glEnd();
                glBegin(GL_LINE_STRIP);
            }

            glVertex2d(x, y);
            lastY = y;
            isFirstPoint = false;
        }

        glEnd();
    }

    private void drawAbsolute() {
        String expression = "";

        if (text.contains("=")) {
            String[] parts = text.split("=", 2);
            expression = parts[1];
        }

        AbsoluteLinearFunction absoluteLinearFunction = new AbsoluteLinearFunction(expression);

        glBegin(GL_LINE_STRIP);
        glColor3f(1.0f, 0.0f, 0.0f); // Barva grafu
        for (double x = X_MIN; x <= X_MAX; x += 0.1d) {
            glVertex2d(x, absoluteLinearFunction.value(x));
        }
        glEnd();
    }

    private void initNumbers() {
        textRenderer.setBackgroundColor(Color.black);
        textRenderer.setColor(Color.white);

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
            start += height / 18;
        }
    }
}
