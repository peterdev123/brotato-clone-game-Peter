package com.mygdx.game.main;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

public class Map {
    private TiledMap map;

    public Map(){
        map = new TmxMapLoader().load("Map/map.tmx");
    }

    public TiledMap getMap(){
        return map;
    }

    public MapObjects getCollissionObjects(){
        MapLayer collisionObjectLayer = map.getLayers().get(3);
        System.out.println(collisionObjectLayer.getObjects().getCount());
        return collisionObjectLayer.getObjects();
    }

    public OrthogonalTiledMapRenderer makeMap(){
        return new OrthogonalTiledMapRenderer(map);
    }

}
