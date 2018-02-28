package reporter

import model.FileWrapper

abstract class ReporterForSeparate extends Reporter{

    protected static String LEFT ="left";
    protected static String RIGHT ="right";
    Map<String,List<FileWrapper>> separateThings = [:]

    List<String> getReportForBothHands() {
        List<String> report = []
        separateThings.forEach{ key, values ->
            report.add(new StringBuilder(key).append(":").append("[").append(prepareFileName( values, key)).append("]").toString())
        }
        return report;
    }

    protected String prepareFileName(List<FileWrapper> codes, String fileHandType) {
        return this.createDelimitedByComas(codes.collect { getFileName(it, fileHandType)})
    }

    protected String getFileName(FileWrapper it, String fileHandType) {
        return "${fileHandType}_${it.code}.${it.kind}"
    }

}
