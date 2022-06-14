package com.up.fintech.armagedon.tp4.strategy;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.Deposit;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

@Service
public final class QrServiceStrategy implements ITransactionStrategy {

	private BufferedImage getQr(Wallet wallet, Transaction transaction) throws WriterException {
		var json = new JSONObject();
		json.appendField("type", transaction.getType());
		json.appendField("walletId", wallet.getWalletId().toString());
		json.appendField("transactionId", transaction.getTransactionId().toString());
		
		if (transaction instanceof Deposit ) 
				json.appendField("confirmationCode", ( (Deposit) transaction).getConfirmationCode());
		if (transaction instanceof Withdraw ) 
			json.appendField("confirmationCode", ( (Withdraw) transaction).getConfirmationCode());
		
		json.appendField("amount", transaction.getAmount());
		json.appendField("email", wallet.getUser().getEmail());
		json.appendField("cuit", wallet.getUser().getCuit());
		json.appendField("timestamp", transaction.getCreatedTime());
		
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(json.toJSONString(), BarcodeFormat.QR_CODE, 200, 200);
		
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		BufferedImage qr;
		try {
			qr = getQr(wallet, transaction);
			((Deposit) transaction).setQr(qr);
			return transaction;
		} catch (WriterException e) {
			throw new TransactionException("No se puede generar QR");
		}
	}

}
