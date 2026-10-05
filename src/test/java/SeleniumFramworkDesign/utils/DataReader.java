package SeleniumFramworkDesign.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import SeleniumFramworkDesign.base.BaseTest;

public class DataReader extends BaseTest {

	public List<HashMap<String, String>> getJsonDataToMap() throws IOException {
		return super.getJsonDataToMap(getTestDataPath("PurchaseOrder.json").toString());
	}
}