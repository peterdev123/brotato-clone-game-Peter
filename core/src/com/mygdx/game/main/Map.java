package com.mygdx.game.main;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

public class Map {
    public final float MAP_WIDTH = 560, MAP_HEIGHT = 400;

    private TiledMap map;

    public Map(){
        map = new TmxMapLoader().load("Map/map.tmx");
    }

    public TiledMap getMap(){
        return map;
    }

    public MapObjects getCollissionObjects(){
        MapLayer collisionObjectLayer = map.getLayers().get(3);
        return collisionObjectLayer.getObjects();
    }

    public MapObjects getBulletCollissionObjects(){
        MapLayer collisionObjectLayer = map.getLayers().get(4);
        return collisionObjectLayer.getObjects();
    }


    public OrthogonalTiledMapRenderer makeMap(){
        return new OrthogonalTiledMapRenderer(map);
    }

}
