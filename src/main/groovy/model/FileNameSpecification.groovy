package model

class FileSpecification {
    String path
    String[] allowFormats

    FileSpecification(path, allowFormats){
        this.path = path
        this.allowFormats =allowFormats
    }
}
