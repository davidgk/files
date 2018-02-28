package reporter

import model.FileSpecification
import model.ValidFileWrapper

class CommonLeftAndRightReporter extends ReporterForSeparate{

    static Reporter create(List<ValidFileWrapper> oneDivision, List<ValidFileWrapper> otherDivision, FileSpecification specification){
        CommonLeftAndRightReporter report = new CommonLeftAndRightReporter()
        report.specification = specification
        def keyForOneDivision = (oneDivision[0].isDivisionA)? specification.divisionPartA: specification.divisionPartB
        def keyForOtherDivision = (oneDivision[0].isDivisionA)? specification.divisionPartB: specification.divisionPartA
        report.separateThings.put(keyForOneDivision,oneDivision)
        report.separateThings.put(keyForOtherDivision,otherDivision)
        report.createReport()
        report
    }

    static Reporter createFromOnlyOneList(List<ValidFileWrapper> onceList, FileSpecification specification){
        CommonLeftAndRightReporter report = new CommonLeftAndRightReporter()
        def keyForOneDivision = (onceList[0].isDivisionA)? specification.divisionPartA: specification.divisionPartB
        def keyForOtherDivision = (keyForOneDivision.equals(specification.divisionPartA))? specification.divisionPartB: specification.divisionPartA
        report.separateThings.put(keyForOneDivision,onceList)
        report.separateThings.put(keyForOtherDivision,onceList)
        report.createReport()
        report
    }

    def createReport() {
        this.report ="["+ createDelimitedByComas(getReportForBothHands())+"]"
    }


}
