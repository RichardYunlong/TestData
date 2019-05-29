package testDemo;

import autotest.dbutil.ConfigManager;
import cfca.ra.datareverter.RADataReverter;

public class CreateDemo {
	public static void main(String[] args) {
		new ConfigManager();
		RADataReverter.doCreate();
	}
}
