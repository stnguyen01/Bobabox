package bobabox.main.Scratches;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.List;

import bobabox.main.GamMenu;
import bobabox.main.Objects.ObjButton;
import bobabox.main.Objects.ObjTables;
import bobabox.main.Sprites.SprGuest;

//NOT IN USE
//Help from Grondin & Daph
//Guests lining up
public class SctStackGuests implements Screen {
    GamMenu gamMenu;

    private OrthographicCamera camera;
    private StretchViewport viewport;
    private SpriteBatch batch;
    private Texture txtBG;
    private ObjTables objTable;
    private boolean isSitting, isTFree = true;
    Vector3 vTouch;
    private SprGuest sprGst;
    private int nTimer = 0, nGst = 0;
    private List<SprGuest> arliGuests;
    private ObjButton btnHome;
    private ShapeRenderer sh;
    private float fY, fH;


    public SctStackGuests(GamMenu _gammenu) {
        gamMenu = _gammenu;

        //Logic
        camera = new OrthographicCamera();
        vTouch = new Vector3();
        viewport = new StretchViewport(1000, 500, camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
        camera.setToOrtho(false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); //camera looks at the center of the screen
        batch = new SpriteBatch();

        //Assets
        objTable = new ObjTables(500, 250, "data/TABLE3_obj.png", "data/TABLE32_obj.png", viewport);
        // objTable2 = new ObjTables(800, 150, "data/TABLE2_obj.png", "data/TABLE22_obj.png", viewport);
        txtBG = new Texture(Gdx.files.internal("data/Test_img.jpg"));
        arliGuests = new ArrayList<SprGuest>();
        btnHome = new ObjButton(900, 30, 260 / 2, 70 / 2, "data/HOME1_btn.png", "data/HOME2_btn.png", viewport);

        for (int i = 1; i <= 10; i++) {
            arliGuests.add(new SprGuest("data/GUEST1_spr.png", viewport));
        }

        sh = new ShapeRenderer();

    }

    @Override
    public void render(float delta) {

        //Logic
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        nTimer++;

        //Setting input
        if (Gdx.input.isTouched()) {
            viewport.unproject(vTouch.set(Gdx.input.getX(), (Gdx.input.getY() * (-1) + 500), 0));
        }

        //Drawing
        batch.draw(txtBG, 0, 0);
        sh.begin(ShapeRenderer.ShapeType.Line);
        sh.setColor(0, 0, 0, 1);
        sh.line(0, 30, 200, 30);
        sh.line(0, 165, 200, 165);
        sh.line(0, 295, 200, 295);
        sh.line(0, 425, 200, 425);
        objTable.draw(batch);
        btnHome.update(batch);
        updateGuest(nGst, batch);
        if (isTFree) {
            if (nTimer % 300 == 0) {
                if (nGst < 5) {
                    nGst++;
                } else {
                    nGst = 0;
                }
                nTimer = 0;
            }
        }
        batch.end();
        sh.end();


        //Buttons
        if (btnHome.justClicked()) {
            gamMenu.updateScreen(2);
        }

    }


    //Runs all of the SprGuests' functions
    private void updateGuest(int nGst, SpriteBatch batch) {
        for (int n = 0; n < nGst; n++) {
            sprGst = arliGuests.get(n); //temporary Guest
            sprGst.draw(batch);
            sprGst.walkDown(nGst);
            sprGst.drag();
            sprGst.sittingDown(isSitting);
            sprGst.hearts(batch, objTable);
        }
    }

    @Override
    public void dispose() {
        txtBG.dispose();
        batch.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(1000 / 2, 500 / 2, 0);
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
}

