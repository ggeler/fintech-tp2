package com.up.fintech.armagedon.tp2.tp2;

import java.io.UnsupportedEncodingException;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestVariables {
	
	private JsonNode wallet;
	private JsonNode transaction;

	private String _link;
	
	public void setTransaction(MvcResult result) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		this.transaction = json.get("data");
	}
	public JsonNode getTransaction() {
		return transaction;
	}
	public void setTransaction(JsonNode node) {
		transaction = node;
	}
	public JsonNode getWallet() {
		return wallet;
	}
	public void setWallet(MvcResult result) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException { //JsonNode jsonWallet) {
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		this.wallet = json.get("data");
	}
	public String getTransactionLinkRel(String rel) {
		return getLinkRel(transaction.get("links"),rel);
	}
	public String getWalletLinkRel(String rel) {
		return getLinkRel(wallet.get("links"),rel);
	}
	private String getLinkRel(JsonNode node, String rel) {
		var linkIterator = node.iterator();
		linkIterator.forEachRemaining(l -> {
			var subNode = l.elements();
			subNode.forEachRemaining(value -> {
				if (value.asText().equals(rel) ) {
						_link = subNode.next().asText();
				}
			});
		});
		var l = _link;
		_link = null;
		return l;
	}
	
}
