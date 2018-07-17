package com.example.permisswion.fx;

class PermissionEntity {
    public Object object;
    public int requestCode;

    public void clear() {
        object = null;
        requestCode = -1;
    }
}
