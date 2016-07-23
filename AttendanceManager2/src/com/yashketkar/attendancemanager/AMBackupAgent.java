package com.yashketkar.attendancemanager;

import java.io.IOException;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.app.backup.BackupAgentHelper;
import android.os.ParcelFileDescriptor;

public class AMBackupAgent extends BackupAgentHelper {
	// The name of the SharedPreferences file
	public static final String PREFS = "MyPrefsFile";
	// A key to uniquely identify the set of backup data
	static final String PREFS_BACKUP_KEY = "prefs";

	// The name of the file
	static final String Attendance = "attendance";

	// A key to uniquely identify the set of backup data
	static final String FILES_BACKUP_KEY = "myfiles";

	// Allocate a helper and add it to the backup agent
	@Override
	public void onCreate() {
		SharedPreferencesBackupHelper shelper = new SharedPreferencesBackupHelper(
				this, PREFS);
		addHelper(PREFS_BACKUP_KEY, shelper);

		FileBackupHelper fhelper = new FileBackupHelper(this, Attendance);
		addHelper(FILES_BACKUP_KEY, fhelper);
	}

	@Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
			ParcelFileDescriptor newState) throws IOException {
		// Hold the lock while the FileBackupHelper performs backup
		synchronized (MainActivity.sDataLock) {
			super.onBackup(oldState, data, newState);
		}
	}

	@Override
	public void onRestore(BackupDataInput data, int appVersionCode,
			ParcelFileDescriptor newState) throws IOException {
		// Hold the lock while the FileBackupHelper restores the file
		synchronized (MainActivity.sDataLock) {
			super.onRestore(data, appVersionCode, newState);
		}
	}
	
	static 
	{
	    try
	    {
	            Class.forName("android.app.backup.BackupManager");
	    }
	    catch (Exception e)
	    {
	        throw new RuntimeException(e);
	    }
	}
}