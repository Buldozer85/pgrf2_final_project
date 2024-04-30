import renderers.Renderer;
import global.LwjglWindow;


public class App {

	public static void main(String[] args) {
		new LwjglWindow(new Renderer(), false);
	}

}
