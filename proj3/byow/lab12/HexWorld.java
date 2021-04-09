package byow.lab12;

import org.junit.Test;

import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static final Random RANDOM = new Random(100);

    public static int hexShift(int s, int i) {
        if (i >= s) {
            i = 2 * s - 1 - i;
        }
        return -i;
    }

    public static int width(int s, int i) {
        if (i >= s) {
            i = 2 * s - i - 1;
        }
        return s + 2 * i;
    }

    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException("hexagon must be at least size 2");
        }
        for (int i = 0; i < 2 * s; i++) {
            int y = p.y + i;
            int x = p.x + hexShift(s, i);
            int width = width(s, i);
            Position newP = new Position(x, y);
            for (int j = 0; j < width; j++) {
                world[newP.x + j][newP.y] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
            }
        }
    }

    public static TETile randomTile() {
        int rand = RANDOM.nextInt(5);
        if (rand == 0) {
            return Tileset.WALL;
        } else if (rand == 1) {
            return Tileset.FLOWER;
        } else if (rand == 2) {
            return Tileset.SAND;
        } else if (rand == 3) {
            return Tileset.FLOOR;
        } else if (rand == 4) {
            return Tileset.GRASS;
        }
        return Tileset.NOTHING;
    }

    public static void drawRandomTile(TETile[][] world, Position p, int s, int t) {
        Position p2 = new Position(p.x, p.y);
        TETile t2 = randomTile();
        for (int i = 0; i < t; i++) {
            p2.y += 2 * s;
            addHexagon(world, p2, s, t2);
            t2 = randomTile();
        }
    }

    public static Position nextTile(Position p, int s) {
        Position newP = new Position(p.x, p.y);
        newP.x += 2 * s - 1;
        newP.y += s;
        return newP;
    }

    public static Position nextTile2(Position p, int s) {
        Position newP = new Position(p.x, p.y);
        newP.x += 2 * s - 1;
        newP.y -= s;
        return newP;
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int s = 3;
        Position p = new Position(s, 2 * s);
        drawRandomTile(world, p, s, 3);
        p = nextTile2(p, s);
        drawRandomTile(world, p, s, 4);
        p = nextTile2(p, s);
        drawRandomTile(world, p, s, 5);
        p = nextTile(p, s);
        drawRandomTile(world, p, s, 4);
        p = nextTile(p, s);
        drawRandomTile(world, p, s, 3);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static class Position {
        private int x;
        private int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
