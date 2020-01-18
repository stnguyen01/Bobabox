package bobabox.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import bobabox.main.GamMenu;
import bobabox.main.Objects.ObjButton;

//Helps navigate between scratches
public class ScrScratch implements Screen {
    GamMenu gamMenu;
    //Values
    int nW , nH ;
    private Vector3 vTouch;
    //Logic
    private OrthographicCamera camera;
    private StretchViewport viewport; //how it's seen
    private SpriteBatch batch;
    private BitmapFont bfFont;
    //Assets
    private Texture txtBg;
    private ObjButton btnHome, btnGuests, btnMultiGuests, btnTap, btnWaiter, btnStackGuests;

    public ScrScratch(GamMenu _gamMenu, StretchViewport _viewport, OrthographicCamera _camera) {
        gamMenu = _gamMenu;


        nH = gamMenu.WORLD_HEIGHT;
        nW = gamMenu.WORLD_WIDTH;
        vTouch = new Vector3();
        viewport = _viewport;
        viewport.apply();
        camera = _camera;
        camera.setToOrtho(false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); //camera looks at the center of the screen
        resize(nW, nH);
        batch = new SpriteBatch();

        bfFont = new BitmapFont(Gdx.files.internal("data/font.fnt"));
        bfFont.setColor(Color.DARK_GRAY);
        bfFont.getData().setScale(0.5f, 0.5f);
        txtBg = new Texture("data/Main_bg.png");
        btnHome = new ObjButton(900, 30, 260 / 2, 70 / 2, "data/HOME1_btn.png", "data/HOME2_btn.png", viewport);
        btnTap = new ObjButton(200, 200, 120, 120, "data/Scratch_btn.png", "data/Scratch_btn.png", viewport);
        btnGuests = new ObjButton(330, 200, 120, 120, "data/Scratch_btn.png", "data/Scratch_btn.png", viewport);
        btnWaiter = new ObjButton(460, 200, 120, 120, "data/Scratch_btn.png", "data/Scratch_btn.png", viewport);
        btnMultiGuests = new ObjButton(590, 200, 120, 120, "data/Scratch_btn.png", "data/Scratch_btn.png", viewport);
        btnStackGuests = new ObjButton(730, 200, 120, 120, "data/Scratch_btn.png", "data/Scratch_btn.png", viewport);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Setup
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isTouched()) {
            vTouch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(vTouch);
        }

        //Drawing
        batch.draw(txtBg, 0, 0, nW, nH);
        btnHome.update(batch);
        btnTap.update(batch);
        bfFont.draw(batch, "Input Test", 155, 205);
        bfFont.draw(batch, "No btnHome", 155, 220);
        btnGuests.update(batch);
        bfFont.draw(batch, "Guest", 310, 205);
        btnWaiter.update(batch);
        bfFont.draw(batch, "Server", 425, 205);
        btnMultiGuests.update(batch);
        bfFont.draw(batch, "MultiGuests", 535, 205);
        btnStackGuests.update(batch);
        bfFont.draw(batch, "StackGuests", 680, 205);
        batch.end();

        //ObjButton
        checkButtons();
    }

    private void checkButtons() { // Checks if Buttons are pressed
        if (btnHome.justClicked()) {
            gamMenu.updateScreen(2);
        }
        if (btnTap.justClicked()) {
            gamMenu.updateScreen(10);
        }
        if (btnGuests.justClicked()) {
            gamMenu.updateScreen(20);
        }
        if (btnWaiter.justClicked()) {
            gamMenu.updateScreen(30);
        }
        if (btnMultiGuests.justClicked()) {
            gamMenu.updateScreen(40);
        }
        if (btnStackGuests.justClicked()) {
            gamMenu.updateScreen(50);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
