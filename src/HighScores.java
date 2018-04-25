import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HighScores {

	private ArrayList<Record> highScores;
	private static final String HIGH_SCORE_RECORD_FILE = "res/highscore.txt";
	static final String DELIMITER = " @@@@@ , @@@@@ ";

	public HighScores() {
		getRecords();		
	}
	
	public void clearRecords() {
		File file = new File(HIGH_SCORE_RECORD_FILE);
		if (file.exists()) {
			file.delete();
		}
	}
	
	private void getRecords() {
		File file = new File(HIGH_SCORE_RECORD_FILE);
		Scanner input = null;
		int row = 0;
		ArrayList<Record> records = new ArrayList<>();
		if(file.exists()) {
			try {
				input = new Scanner(file);
				while (input.hasNextLine()) {
					String[] record = input.nextLine().split(DELIMITER);
					records.add(new Record(Integer.valueOf(record[0]), Integer.valueOf(record[1]), record[2], row));
					row++;
				}
				Collections.sort(records);
			} catch (FileNotFoundException e) {
				System.out.println(e.getStackTrace());
			} finally {
				if (input != null) {
					input.close();
				}
			}
		}
		this.highScores = records;		
	}
	
	public void save(ArrayList<Record> highScores) {
		PrintWriter output = null;
		try {
			output = new PrintWriter(HIGH_SCORE_RECORD_FILE);
			if (highScores == null || highScores.size() == 0) {
			} else {
				Collections.sort(highScores);
				for (int i = 0; i < highScores.size(); i++) {
					output.println(highScores.get(i).toString());
				}
			}
		} catch (FileNotFoundException e) {
//			throw new FileNotFoundException("Failed to save the highscore file.");
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
	
	public Record getHighScore(int level) {
		if (highScores == null || highScores.size() == 0) return null;
		for (Record record : highScores) {
			if (record.getLevel() == level) {
				return record;
			}
		}
		return null;
	}
	
	public ArrayList<Record> getHighScoreList() {
		return this.highScores;
	}
}


class Record implements Comparable<Record> {
	private int level;
	private int time;
	private String name;
	private int index;
	Record(int level, int time, String name) {
		this.level = level;
		this.time = time;
		this.name = name;
		this.index = 0;
	}
	Record(int level, int time, String name, int index) {
		this.level = level;
		this.time = time;
		this.name = name;
		this.index = index;
	}
	public int getLevel() {
		return level;
	}
	public int getTime() {
		return time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public String getLevelName() {
		String levelName = "";
		switch (level) {
		case 1 : 
			levelName = "Begginer";
			break;
		case 2 : 
			levelName = "Intermediate";
			break;
		case 3 : 
			levelName = "Expert";
			break;
		}
		return levelName;
	}
	
	@Override
	public int compareTo(Record o) {
		if (level > o.level) {
			return 1;
		} else if (level < o.level) {
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return level + HighScores.DELIMITER + time + HighScores.DELIMITER + name;
	}
}
