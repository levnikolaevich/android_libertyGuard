package ru.libertyguard.libertyguard;

import java.io.File;

import android.os.Environment;

import ru.libertyguard.libertyguard.AlbumStorageDirFactory;

public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

    @Override
    public File getAlbumStorageDir(String albumName) {
        // TODO Auto-generated method stub
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );
    }
}
