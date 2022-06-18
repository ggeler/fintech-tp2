package com.up.fintech.armagedon.tp2.tp2;

import com.fasterxml.jackson.databind.JsonNode;

public class TestVariables {
	
	private JsonNode jsonWallet;
	//private String walletId;

	private String _link;
	
	public JsonNode getJsonWallet() {
		return jsonWallet;
	}
	public void setJsonWallet(JsonNode jsonWallet) {
		this.jsonWallet = jsonWallet;
		//this.walletId = jsonWallet.get("walletId").asText();
	}
//	public String getWalletId() {
//		return walletId;
//	}
	public String getLinkRel(JsonNode node, String rel) {
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
