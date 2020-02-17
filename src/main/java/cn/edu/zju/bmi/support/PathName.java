package cn.edu.zju.bmi.support;

public class PathName {
    //group Analysis
    public static final String GROUP_ANALYSIS_DATA = "/backend/data/groupAnalysis";
    public static final String QUERY_WITH_FILTER = "/queryWithFilter";
    public static final String GET_VISIT_INFO = "/getVisitInfo";


    //individual Analysis
    public static final String INDIVIDUAL_ANALYSIS_DATA = "/backend/data/individualAnalysis";
    public static final String INDIVIDUAL_ANALYSIS_DATA_UNIFIED_PATIENT_ID = "/unifiedPatientID";
    public static final String INDIVIDUAL_ANALYSIS_DATA_BASIC_INFO ="/basicInfo";
    public static final String INDIVIDUAL_ANALYSIS_DATA_TRAJECTORY ="/trajectory";
    public static final String INDIVIDUAL_ANALYSIS_DATA_VISIT_DETAILED_INFO ="/visitDetailedInfo";
    public static final String INDIVIDUAL_ANALYSIS_DATA_LAB_TEST_SINGLE_VISIT ="/labTestSingleVisit";
    public static final String INDIVIDUAL_ANALYSIS_DATA_LAB_TEST_SINGLE_ITEM_IN_ONE_VISIT ="/labTestSingleItemInOneVisit";
    public static final String INDIVIDUAL_ANALYSIS_DATA_LAB_TEST_SINGLE_ITEM_TRACE ="/labTestSingleItemTrace";
    public static final String INDIVIDUAL_ANALYSIS_DATA_ORDER ="/order";
    public static final String INDIVIDUAL_ANALYSIS_DATA_VITAL_SIGN ="/vitalSign";
    public static final String INDIVIDUAL_ANALYSIS_DATA_EXAM ="/exam";
    public static final String LAB_TEST_NAME_DICT = "/labTestNameList";

    // Auth
    public static final String AUTH = "/backend/authentic";
    public static final String LOGIN = "/login";
    public static final String SIGNUP = "/signup";
    public static final String SIGNUP_USER_EXIST_TEST = "/signupUserExistTest";

    // user info
    public static final String USER_INFO_DATA = "/backend/data/userInfo";
    public static final String USER_INFO = "/userInfo";

    // algorithm management
    public static final String ALGORITHM_MANAGEMENT="/backend/algorithmManagement";
    public static final String UPLOAD_MODEL_FILE = "/uploadModelFile";
    public static final String DOWNLOAD_MODEL_FILE = "/downloadModelFile";
    public static final String UPLOAD_PREPROCESSING_MODULE = "/uploadPreprocessingModule";
    public static final String DOWNLOAD_PREPROCESSING_MODULE = "/downloadPreprocessingModule";
    public static final String UPLOAD_MODEL_DOCUMENT = "/uploadModelDoc";
    public static final String DOWNLOAD_MODEL_DOCUMENT="/downloadModelDoc";
    public static final String CREATE_NEW_MODEL= "/createNewModel";
    public static final String DELETE_EXIST_MODEL = "/deleteExistModel";
    public static final String FETCH_MODEL_LIST = "/fetchModelList";
    public static final String UPDATE_ACCESS_CONTROL = "/updateAccessControl";
    public static final String UPDATE_PLATFORM = "/updatePlatform";
    public static final String MODEL_INFO = "/modelInfo";

    // state management
    public static final String STATE_MANAGEMENT="/backend/stateManagement";
    public static final String UPDATE_STATE = "/uploadState";
    public static final String DOWNLOAD_STATE = "/downloadState";

    // machine learning path
    public static final String INVOKE_MACHINE_LEARNING_SERVICE = "/backend/machineLearning";
    public static final String FETCH_MACHINE_LEARNING_MODEL_DATA = "/getDataForMachineLearningModelUpToOneVisit";
    public static final String EXECUTE_MACHINE_LEARNING_MODEL = "/executeModel";
}
