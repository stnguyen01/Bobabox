package bobabox.main.Scratches;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

//https://www.youtube.com/watch?v=qlJUrcpQXo8
//DOESN'T HAVE A BACK BUTTON
//Demonstrates tapping on android

public class SctTap implements Screen {

    private OrthographicCamera camera;
    private ShapeRenderer sr;
    private Vector3 pos; //vect with 3 values (position)

    public SctTap(Game _gammenu) {

        sr = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //y values will increase going up
        pos = new Vector3(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

    }

    @Override
    public void resize(int width, int height) {
    }

    public void render(float delta) {

        //Logic
        camera.update();
        if (Gdx.input.isTouched()) {
            pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(pos);
        }

        //Drawing
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLUE);
        sr.circle(pos.x, pos.y, 64);
        sr.end();
    }

    public void show() {
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
        sr.dispose();
    }
}
