package com.company;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.awt.TextRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

public class data_visualiser {
    private static GLCanvas canvas;
    private static int windowsCount;

    private static void init() {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        Background bkg = new Background();
        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(bkg);
        canvas.setSize(1200, 1200);
    }

    private static void setAndShowWindow(String name) {
        final JFrame frame = new JFrame(name);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (windowsCount == 1)
                    System.exit(0);
                else
                    windowsCount--;
            }
        });
        frame.getContentPane().add(canvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        windowsCount++;
    }

    public static void main(String[] args) {
        init();
        canvas.addGLEventListener(new Circle(0f, 0f, 0.01f, 128));
        canvas.addGLEventListener(new Text(650, 600, 15, 73, "Pipis died in Irak on Jun 17 2020"));
        //drawOriginalDataText(0, 0, 0, 0);
        setAndShowWindow("Hahajomba");
    }
    private static void drawOriginalDataText(double minX, double minY, double maxX, double maxY) {
        var canW = canvas.getSize().width;
        var canH = canvas.getSize().height;

        canvas.addGLEventListener(new Text(canW * 0.05f, canH * 0.05f, 12, 45, "" + (int)minX));
    }
}

class Circle implements GLEventListener {
    private final float detailLevel;
    private final float cx;
    private final float cy;
    private final float r;

    public Circle(float cx, float cy, float r, int detailLevel) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.detailLevel = detailLevel;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        var twicePI = Math.PI * 2;
        var detailFactor = twicePI/detailLevel;

        gl.glColor3f(0.5f, 0.5f, 0.5f);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy);
        for (var i = 0; i <= detailLevel; i++)
            gl.glVertex2f((float) (cx + (r * Math.cos(i * detailFactor))), (float) (cy + (r * Math.sin(i * detailFactor))));
        gl.glEnd();
        gl.glColor3f(1f, 1f, 1f);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy);
        for (var i = 0; i <= detailLevel; i++)
            gl.glVertex2f((float) (cx + (r * 0.90 * Math.cos(i * detailFactor))), (float) (cy + (r * 0.90 * Math.sin(i * detailFactor))));
        gl.glEnd();
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    @Override
    public void dispose(GLAutoDrawable arg0) {}
    @Override
    public void init(GLAutoDrawable arg0) {}
}
class Background implements GLEventListener {
    @Override
    public void init(GLAutoDrawable arg0) { }
    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f( 1f,1f,1f );
        gl.glVertex3f(-1f, -1f, 0);
        gl.glVertex3f(1f, -1f, 0);
        gl.glVertex3f(1f, 1f, 0);
        gl.glVertex3f(-1f, 1f, 0);
        gl.glEnd();
    }
    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) { }
    @Override
    public void dispose(GLAutoDrawable arg0) { }
}
class Line implements GLEventListener {
    private final float width;
    private final float x0;
    private final float y0;
    private final float x1;
    private final float y1;
    //Приделать хахаЦвет TODO
    public Line(float x0, float y0, float x1, float y1, float width) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glLineWidth(width);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f( 0.5f,0.5f,1.0f );
        gl.glVertex3f(x0, y0, 0);
        gl.glVertex3f(x1, y1, 0);
        gl.glEnd();
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    @Override
    public void dispose(GLAutoDrawable arg0) {}
    @Override
    public void init(GLAutoDrawable arg0) {}
}
class Text implements GLEventListener {
    private final String text;
    private final float angle;
    private final float x0;
    private final float y0;
    private final int fontSize;

    public Text(float x0, float y0, int fontSize, String text) {
        this.text = text;
        this.x0 = x0;
        this.y0 = y0;
        this.fontSize = fontSize;
        this.angle = 0.0f;
    }

    public Text(float x0, float y0, int fontSize, float angle, String text) {
        this.text = text;
        this.x0 = x0;
        this.y0 = y0;
        this.fontSize = fontSize;
        this.angle = angle;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        var textRenderer = new TextRenderer(new Font("Arial", Font.BOLD, fontSize));

        textRenderer.setColor(0f,0f,0f,1f);
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glTranslatef(x0, y0, 0f);
        gl.glRotatef(angle, 0f, 0f, 1f);
        textRenderer.draw(text, 0, 0);
        textRenderer.endRendering();
        gl.glPopMatrix();
        gl.glFlush();
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    @Override
    public void dispose(GLAutoDrawable arg0) {}
    @Override
    public void init(GLAutoDrawable arg0) {}
}