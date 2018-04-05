package main.java;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import main.utils.APIResources;

public class Account {
	/* Parameters */
	private String familyName;
	private String joined;
	private String lastContributed;
	private String memberType;
	private String status;
	private Integer contribution;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

	public Account(String name) throws ParseException {
		this.familyName = name;
		this.joined = dateFormat.format(new Date());
		this.lastContributed = null;
		this.memberType = "Member";
		this.status = "Active";
		this.contribution = 0;
	}

	public Account() throws ParseException {
		this.familyName = "";
	}

	// Return a joined date of an account
	public String getJoinedDate() {
		return this.joined;
	}
	
	public void setJoinedDate(String value) {
		this.joined = value;
	}
	
	public void setLastContribution(String value) {
		this.lastContributed = value;
	}

	// Return the last forum visited date of an account
	public String getLastContribution() {
		if(this.lastContributed == null) {
				return this.joined;
			}
		else {
			return this.lastContributed;
			}
	}

	// Return the type of a player
	public String getMemberType() {
		return this.memberType;
	}
	public void promoteMember() {
		this.memberType = "Officer";
	}
	public void demoteMember() {
		this.memberType = "Member";
	}
	
	public String getSatus() {
		return this.status;
	}

	// Return the last ladder online date of an account from all the characters,
	// using api.exiletools.com
	public int getContribution() {
		return this.contribution;
	}
	public int addContribution() {
		contribution++;
		this.lastContributed = dateFormat.format(new Date());
		return this.contribution;
	}
	public int removeContribution() {
		contribution--; 
		return this.contribution;
	}
	public void setContribution(int points) {
		contribution = points;
	}
	public void resetContribution() {
		contribution = 0 ;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public void setFamilyName(String name) {
		this.familyName = name;
	}
	
	public void setMemberType(String type) {
		this.memberType = type;
	}
	public void setStatus(String value) {
		this.status = value;
	}

	public void setValue(int j, String value) {
		switch(j) {
		case 1:
			setFamilyName(value);
			break;
		case 2:
			setMemberType(value);
			break;
		case 3:
			setStatus(value);
			break;
		case 4:
			setContribution(Integer.valueOf(value));
			break;
		case 6:
			setJoinedDate(value);
			break;
		case 7:
			setLastContribution(value);
			break;
		default:
			break;
		}
	
		// TODO Auto-generated method stub
		
	}
	public void setValue(int j, int value) {
		switch(j) {
		case 4:
			setContribution(value);
			break;
		default:
			break;
		}
	}
	

	
	// Return the Poe.Trade Online status of an account, using a brute-force
	// query trick (slow speed)
	/*
	public boolean getPoeTradeOnlineStatus() throws IOException {
		this.poeTradeOnline = false;
		String[] leagues = APIResources.getActiveLeaguesName();

		for (String league : leagues) {
			RequestBody formBody = new FormEncodingBuilder().add("league", league).add("seller", this.profile)
					.add("online", "x").build();
			Document jsoupDoc = Jsoup.parse(HttpClient.runURLWithRequestBody("http://poe.trade/search", formBody));
			if (!jsoupDoc.getElementsByClass("search-results-block").text().equals("")) {
				this.poeTradeOnline = true;
				break;
			}
		}
		return this.poeTradeOnline;
	}

	// Return a String contain Supporter Tags of an account
	public String getSupporterTagKeys() {
		String tagKeys = "";

		for (Element tag : details.getElementsByAttributeValueContaining("class", "roleLabel")) {
			try {
				String imgSrc = tag.child(0).attr("src");
				String imgFileName = imgSrc.substring(imgSrc.lastIndexOf('/') + 1, imgSrc.length());
				String imgKey = imgFileName.substring(0, imgFileName.lastIndexOf('.')).toLowerCase().replace('-', '_');
				tagKeys += imgKey + " ";
			} catch (java.lang.IndexOutOfBoundsException e) {
				// Skip on IndexOutOfBoundsException occur
				// e.printStackTrace();
			}
		}
		return tagKeys.trim();
	}

	//
	public String getStatus() {
		StringBuilder status = new StringBuilder();
		String delim = "";
		for (Element tag : details.getElementsByAttributeValueContaining("class", "roleLabel")) {
			if (tag.attr("class").equals("roleLabel valuedPosterText")) {
				status.append(delim).append("Valued Poster");
				delim = " | ";
				continue;
			}
			if (tag.attr("class").equals("roleLabel in-alpha")) {
				status.append(delim).append("Alpha Member");
				delim = " | ";
				continue;
			}
			if (tag.attr("class").equals("roleLabel onProbation")) {
				status.append(delim).append("On Probation");
				delim = " | ";
				continue;
			}
			if (tag.attr("class").equals("roleLabel banned")) {
				status.append(delim).append("Banned");
				delim = " | ";
				continue;
			}
		}
		if (!status.toString().equals(""))
			return "(" + status.toString().trim() + ")";
		else
			return "";
	}*/
}
