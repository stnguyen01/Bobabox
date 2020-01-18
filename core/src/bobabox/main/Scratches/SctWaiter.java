package bobabox.main.Scratches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import bobabox.main.GamMenu;
import bobabox.main.Objects.ObjBar;
import bobabox.main.Objects.ObjButton;
import bobabox.main.Objects.ObjTables;
import bobabox.main.Sprites.SprServer;

//Sarah
//Help from Grondin & Daph
//testing tap, response function from the waiter
public class SctWaiter implements Screen, InputProcessor {

    //Logic
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private StretchViewport viewport;
    Vector2 vTouch;
    //Assets
    private Texture txBG;
    private SprServer sprServer;
    private ObjButton btnHome;
    private GamMenu gamMenu;
    private ShapeRenderer sh;
    private ObjBar objBar;
    private ObjTables objTable, arTables[] = new ObjTables[3], objTableServed;
    //Values
    private int nWORLD_WIDTH, nWORLD_HEIGHT, nTargetTble;
    private float fXG, fYG;
    private boolean isTableClicked = false, bArrived = false;

    public SctWaiter(GamMenu _gammenu) {
        gamMenu = _gammenu;

        //game height and width
        nWORLD_WIDTH = 1000;
        nWORLD_HEIGHT = 500;

        //camera
        camera = new OrthographicCamera();
        vTouch = new Vector2();
        viewport = new StretchViewport(1000, 500, camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
        camera.setToOrtho(false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); //camera looks at the center of the screen
        resize(nWORLD_WIDTH, nWORLD_HEIGHT);

        //textures
        txBG = new Texture("data/GameBG_img.png");

        //sprites
        batch = new SpriteBatch();

        //bar
        objBar = new ObjBar(viewport, new Rectangle(300, 300, 450, 80));

        //table
        arTables[0] = new ObjTables(280, 80, "data/TABLE1_obj.png", "data/TABLE12_obj.png", viewport);
        arTables[1] = new ObjTables(nWORLD_WIDTH / 2, 50, "data/TABLE2_obj.png", "data/TABLE22_obj.png", viewport);
        arTables[2] = new ObjTables(nWORLD_WIDTH - 280, 80, "data/TABLE3_obj.png", "data/TABLE32_obj.png", viewport);

        //server
        fXG = 850;
        fYG = 175;
        sprServer = new SprServer(fXG, fYG); //850, 175

        //Buttons
        btnHome = new ObjButton(900, 30, 260 / 2, 70 / 2, "data/HOME1_btn.png", "data/HOME2_btn.png", viewport);

        //Shape Renderer
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/ShapeRenderer.html
        sh = new ShapeRenderer();
    }

    @Override
    public void show() {
    }

    public void render(float delta) {

        //set up
        camera.update();
        batch.begin();
        sh.begin(ShapeRenderer.ShapeType.Line);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(txBG, 0, 0, nWORLD_WIDTH, nWORLD_HEIGHT);
        Gdx.input.setInputProcessor(this);
        sh.setColor(0, 0, 0, 1);

        //drawing
        sprServer.update(fXG, fYG, batch, false);
        btnHome.draw(batch);

        //Rectangle for Server
        sh.rect(sprServer.getX(), sprServer.getY(), sprServer.getWidth(), sprServer.getHeight());

        //if home button clicked. Goes back to home screen
        if (btnHome.justClicked()) {
            gamMenu.updateScreen(2);
        }

        //Checks if bar is clicked
        if (objBar.isTapped()) {
            System.out.println("Bar is touched");
            fXG = objBar.rBar().x + 1;
            fYG = objBar.rBar().y - 20;
            sprServer.update(fXG, fYG, batch, false); //isCstDragged is false for testing
        }

        //Checks if Table is Clicked
        if (isTableClicked) {
            System.out.println("X GOAL " + fXG + "Y GOAL " + fYG);
            sprServer.update(fXG, fYG, batch, false);
            bArrived = sprServer.arrived();
        }

        updateTable();
        batch.end();
        sh.end();
    }

    //Method runs through the array of tables
    private void updateTable() {
        for (int i = 0; i < 3; i++) {
            objTable = arTables[i]; //temp. table
            objTable.draw(batch);
            sh.rect(objTable.getX(), objTable.getY(), objTable.getWidth(), objTable.getHeight());
            // Checks if mouse is over table and clicked
//            if (objTable.isTableClicked()) {
//
//                fXG = Math.round(arTables[i].getX() + (objTable.getWidth() / 2 - 40));
//                fYG = Math.round(arTables[i].getY() + objTable.getHeight());
//
//                isTableClicked = true;
//
//            }
//
//            //Debugging stuff
//            if (arTables[0].isTableClicked()) {
//                System.out.println("TABLE 11111111 WAS CLICKEDD");
//            }
//            if (arTables[1].isTableClicked()) {
//                System.out.println("TABLE 22222222 WAS CLICKEDD");
//            }
//            if (arTables[2].isTableClicked()) {
//                System.out.println("TABLE 33333333 WAS CLICKEDD");
//            }

        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(vTouch.set(Gdx.input.getX(), Gdx.input.getY()));
//        System.out.println("tappp");
        for (int i = 0; i < 3; i++) {
            objTable = arTables[i]; //temp. table
            if (objTable.getBoundingRectangle().contains(vTouch)) {
                nTargetTble = i;
                objTableServed = arTables[nTargetTble];

                fXG = Math.round(objTableServed.getX() + (objTableServed.getWidth() / 2 - 40));
                fYG = Math.round(objTableServed.getY() + objTableServed.getHeight());
                System.out.println("TABLE " + i + " WAS CLICKED");

                isTableClicked = true;
            }
        }

        return false;
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

    @Override
    public void dispose() {
        txBG.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}