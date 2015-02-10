import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

class Colour {
	public int r, g, b, a, raw;
	
	public Colour(int col) {
		raw = col;
		r = col & 0xFF;
		g = col >> 8 & 0xFF;
		b = col >> 16 & 0xFF;
		a = col >> 24 & 0xFF;
	}
	
	public Colour(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
}
		