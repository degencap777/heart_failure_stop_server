package cn.edu.zju.bmi.service;
import cn.edu.zju.bmi.support.ParameterName;
import cn.edu.zju.bmi.entity.DAO.*;
import cn.edu.zju.bmi.entity.DAO.Exam;
import cn.edu.zju.bmi.entity.POJO.*;
import cn.edu.zju.bmi.repository.*;
import cn.edu.zju.bmi.support.EchoCardioElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GetSinglePatInfoService {
    private DiagnosisRepository diagnosisRepository;
    private LabTestRepository labTestRepository;
    private OperationRepository operationRepository;
    private OrdersRepository ordersRepository;
    private PatientVisitRepository patientVisitRepository;
    private VitalSignRepository vitalSignRepository;
    private PatientRepository patientRepository;
    private ExamRepository examRepository;
    private IdMappingRepository idMappingRepository;
    private HospitalMapRepository hospitalMapRepository;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Autowired
    public GetSinglePatInfoService(DiagnosisRepository diagnosisRepository, LabTestRepository labTestRepository,
                                   OperationRepository operationRepository, OrdersRepository ordersRepository,
                                   PatientRepository patientRepository, VitalSignRepository vitalSignRepository,
                                   PatientVisitRepository patientVisitRepository, ExamRepository examRepository,
                                   IdMappingRepository idMappingRepository, HospitalMapRepository hospitalMapRepository)
    {
        this.diagnosisRepository = diagnosisRepository;
        this.labTestRepository = labTestRepository;
        this.operationRepository = operationRepository;
        this.ordersRepository = ordersRepository;
        this.patientRepository = patientRepository;
        this.vitalSignRepository = vitalSignRepository;
        this.idMappingRepository = idMappingRepository;
        this.patientVisitRepository = patientVisitRepository;
        this.examRepository = examRepository;
        this.hospitalMapRepository = hospitalMapRepository;
    }

    public String getUnifiedPatientID(String patientID, String hospitalCode){
        IdMapping idMapping = idMappingRepository.findIdMappingByHospitalPatIDAndHospitalCode(patientID, hospitalCode);
        String unifiedPatientID = ParameterName.NO_UNIFIED_PATIENT_ID_FOUND;
        if(idMapping != null){
            unifiedPatientID = idMapping.getUnifiedPatientID();
        }
        return unifiedPatientID;
    }

    public PatientBasicInfo getPatientBasicInfo(String unifiedPatientID){
        Patient patGeneralInfo = patientRepository.findPatientByUnifiedPatientID(unifiedPatientID);
        String patName = patGeneralInfo.getName();
        String sex = patGeneralInfo.getSex();

        String birthday = sdf1.format(patGeneralInfo.getBirthday());

        String ethnicGroup = patGeneralInfo.getEthnicGroup();
        return new PatientBasicInfo(patName, sex, birthday, ethnicGroup);
    }

    public List<VisitInTrajectory> getPatientTrajectory(String unifiedPatientID){
        List<VisitInTrajectory> visitInTrajectories = new ArrayList<>();

        List<PatientVisit> validVisit = patientVisitRepository.findAllByKeyUnifiedPatientID(unifiedPatientID);
        for(int i = 0; i < validVisit.size(); i++){
            PatientVisit patientVisit = validVisit.get(i);
            String visitNo = String.valueOf(i);
            String admissionTime = sdf1.format(patientVisit.getAdmissionDateTime());
            String visitType = patientVisit.getKey().getVisitType();
            String visitID = patientVisit.getKey().getVisitID();
            String hospitalCode_ = patientVisit.getKey().getHospitalCode();
            VisitInTrajectory singleVisit = new VisitInTrajectory(visitNo, admissionTime, hospitalCode_, visitType,
                    visitID);
            visitInTrajectories.add(singleVisit);
        }
        return visitInTrajectories;
    }

    public VisitBriefInfo getVisitBriefInfo(String unifiedPatientID, String hospitalCode, String visitType,
                                                  String visitID){
        PatientVisit patientVisit = patientVisitRepository.
                findPatientVisitByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCode(
                        unifiedPatientID, visitID, visitType, hospitalCode);
        String admissionTime = sdf2.format(patientVisit.getAdmissionDateTime());
        String dischargeTime = sdf2.format(patientVisit.getDischargeDateTime());
        String hospitalName = hospitalMapRepository.findHospitalMapByHospitalCode(hospitalCode).getHospitalName();
        String deathFlag = patientVisit.getDeathFlag();

        List<Diagnosis> mainDiagnosis =
                diagnosisRepository.findAllByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCodeAndKeyDiagnosisTypeOrderByKeyDiagnosisNo(
                        unifiedPatientID, visitID, visitType, hospitalCode, "3");
        String mainDiagnosisStr = "无主诊断";
        if (mainDiagnosis.size() > 0)
            mainDiagnosisStr = mainDiagnosis.get(0).getDiagnosisDesc();

        String symptom = "No Data";
        return new VisitBriefInfo(admissionTime, dischargeTime, hospitalName, mainDiagnosisStr, symptom, deathFlag);
    }

    public VisitDetailedInfo getVisitDetailedInfo(String unifiedPatientID, String hospitalCode, String visitType,
                                                  String visitID){
        Patient patGeneralInfo = patientRepository.findPatientByUnifiedPatientID(unifiedPatientID);
        List<Diagnosis> mainDiagnosisList =
                diagnosisRepository.findAllByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCodeAndKeyDiagnosisTypeOrderByKeyDiagnosisNo(
                unifiedPatientID, visitID, visitType, hospitalCode, "3");
        List<Diagnosis> otherDiagnosisList = diagnosisRepository.findAllByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCodeAndKeyDiagnosisTypeOrderByKeyDiagnosisNo(
                unifiedPatientID, visitID, visitType, hospitalCode, "A");
        PatientVisit visitInfo = patientVisitRepository.findPatientVisitByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCode(
                unifiedPatientID, visitID, visitType, hospitalCode);
        List<Operation> operationList = operationRepository.findByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCode(
                unifiedPatientID, visitID, visitType, hospitalCode);

        List<String> mainDiagnosisStrList = new ArrayList<>();
        for (Diagnosis diagnosis: mainDiagnosisList)
            mainDiagnosisStrList.add(diagnosis.getDiagnosisDesc());
        List<String> otherDiagnosisStrList = new ArrayList<>();
        for (Diagnosis diagnosis: otherDiagnosisList)
            otherDiagnosisStrList.add(diagnosis.getDiagnosisDesc());
        List<String> operationStrList = new ArrayList<>();
        for (Operation operation: operationList)
            operationStrList.add(operation.getOperationDesc());

        String patName = patGeneralInfo.getName();
        String sex = patGeneralInfo.getSex();
        Date birthday = patGeneralInfo.getBirthday();
        Date visitTime = visitInfo.getAdmissionDateTime();
        String age = String.valueOf((int) ((visitTime.getTime() - birthday.getTime()) / (1000 * 60 * 60 * 24)) / 365);
        String hospitalName = getHospitalName(hospitalCode);
        String admissionTime = sdf1.format(visitInfo.getAdmissionDateTime());
        String dischargeTime = sdf1.format(visitInfo.getDischargeDateTime());

        return new VisitDetailedInfo (patName, sex, age, hospitalName, visitType, visitID,
                admissionTime, dischargeTime, mainDiagnosisStrList, operationStrList, otherDiagnosisStrList);
    }

    public Map<String, List<LabTestResult>> getLabTest(String unifiedPatientID, String hospitalCode, String visitType,
                                                         String visitID){
        Map<String, List<LabTestResult>> labTestMap = new HashMap<>();
        List<LabTest> labTestList =
                labTestRepository.findByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCode(
                        unifiedPatientID, visitID, visitType, hospitalCode);
        for (LabTest labTest : labTestList){
            String itemName = labTest.getLabTestItemName();
            if (!labTestMap.containsKey(itemName))
                labTestMap.put(itemName, new ArrayList<>());

            String result = labTest.getResult();
            String unit = labTest.getUnits();
            Date testTime = labTest.getExecuteDate();
            LabTestResult labTestResult = new LabTestResult(itemName, result, unit, testTime);
            labTestMap.get(itemName).add(labTestResult);
        }
        return labTestMap;
    }

    public Map<String, List<MedicalOralIntervention>> getMedicalOralIntervention(String unifiedPatientID, String hospitalCode, String visitType,
                                                                                 String visitID){
        List<Orders> medicineOrderList = ordersRepository.findByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCodeAndOrderClass(
                unifiedPatientID, visitID, visitType, hospitalCode, "A");
        Map<String, List<MedicalOralIntervention>> ordersMap = new HashMap<>();
        for (Orders orders : medicineOrderList){
            String itemName = orders.getOrderText();
            if (!ordersMap.containsKey(itemName))
                ordersMap.put(itemName, new ArrayList<>());
            String startTime = sdf2.format(orders.getStartDateTime());
            String endTime = sdf2.format(orders.getStopDateTime());
            String unit = orders.getDosageUnit();
            String dosage = orders.getDosage();
            String dosageWithUnit = dosage+"_"+unit;
            String frequency = orders.getFrequency();
            MedicalOralIntervention order = new MedicalOralIntervention(itemName, dosageWithUnit, frequency, startTime, endTime);
            ordersMap.get(itemName).add(order);
        }
        return ordersMap;
    }

    public Map<String, List<VitalSignResult>> getVitalSignOfVisit(String unifiedPatientID, String hospitalCode, String visitType,
                                                             String visitID){
        Map<String, List<VitalSignResult>> vitalSignMap = new HashMap<>();
        List<VitalSign> vitalSignList =
                vitalSignRepository.findByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCode(
                        unifiedPatientID, visitID, visitType, hospitalCode);
        for (VitalSign vitalSign : vitalSignList){
            String itemName = vitalSign.getKey().getVitalSign();
            if (! vitalSignMap.containsKey(itemName))
                vitalSignMap.put(itemName, new ArrayList<>());
            String name = vitalSign.getKey().getVitalSign();
            String value = String.valueOf(vitalSign.getResult());
            Date recordTime = vitalSign.getRecordTime();
            String unit = vitalSign.getUnit();
            VitalSignResult vitalSignResult = new VitalSignResult(name, recordTime, value, unit);
            vitalSignMap.get(itemName).add(vitalSignResult);
        }
        return vitalSignMap;
    }

    public List<ExamResult> getExamResult(String unifiedPatientID, String hospitalCode, String visitType,
                                          String visitID) {
        List<Exam> examList =
                examRepository.findByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCode(
                        unifiedPatientID, visitID, visitType, hospitalCode);
        List<ExamResult> examResultList = new ArrayList<>();
        for(Exam exam : examList){
            String examName = exam.getExamName();
            Date examTime = exam.getExamDateTime();
            String exam_para = exam.getExamPara();
            String impression = exam.getImpression();
            String description = exam.getDescription();
            ExamResult examResult = new ExamResult(examName, examTime, exam_para, impression, description);
            examResultList.add(examResult);
        }
        return examResultList;
    }

    private String getHospitalName(String hospitalCode){
        HospitalMap hospitalMap = hospitalMapRepository.findHospitalMapByHospitalCode(hospitalCode);
        return hospitalMap.getHospitalName();
    }

    // TODO
    private List<EchoCardioElements> getStructuredEchoCardioGramOfVisit(String unifiedPatientID, String hospitalCode, String visitType,
                                                                        String visitID){
        List<Exam> examList =
                examRepository.findByKeyUnifiedPatientIDAndKeyVisitIDAndKeyVisitTypeAndKeyHospitalCode(
                        unifiedPatientID, visitID, visitType, hospitalCode);
        return echoCardioGramStructuralize(examList);
    }

    // TODO
    private List<EchoCardioElements> echoCardioGramStructuralize(List<Exam> examList){
        List<EchoCardioElements> echoCardioEleList = new ArrayList<>();
        for (Exam exam : examList)
            System.out.println(exam);
            echoCardioEleList.add(new EchoCardioElements());
        return echoCardioEleList;
    }
}
