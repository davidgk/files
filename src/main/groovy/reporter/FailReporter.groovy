package reporter

import model.ValidFileWrapper

class FailReporter extends ReporterForSeparate{

    String error

    static Reporter create(String error, List<ValidFileWrapper> errors, specification){
        FailReporter report = new FailReporter()
        report.specification = specification
        def keyForOneDivision = (errors[0].isDivisionA)? specification.divisionPartA: specification.divisionPartB
        def keyForOtherDivision = (keyForOneDivision.equals(specification.divisionPartA))? specification.divisionPartB: specification.divisionPartA
        report.separateThings.put(keyForOneDivision,errors)
        report.separateThings.put(keyForOtherDivision,errors)
        report.error = error
        report.createReport()
        report
    }

    def createReport() {
        StringBuilder builder = new StringBuilder("{error:")
            builder.append("'").append(error).append("'").append(",")
            .append(createDelimitedByComas(getReportForBothHands()))
            .append("}")
        this.report =builder.toString()
    }

}
