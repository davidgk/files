package reporter

import model.FileSpecification
import model.FileWrapper
import model.ValidFileWrapper

abstract class ReporterForSeparate extends Reporter{

    Map<String,List<FileWrapper>> separateThings = [:]
    FileSpecification specification

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

    protected String getFileName(ValidFileWrapper it, String fileHandType) {
        return "${fileHandType}_${it.code}.${it.kind}"
    }

}
