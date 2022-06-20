package com.up.fintech.armagedon.tp4.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.repository.IBetRepository;

@Service
public class BetService {

	private final IBetRepository betRepository;
	private final WalletService walletService;
	
	public BetService(WalletService walletService, IBetRepository betRepository) {
		this.betRepository = betRepository;
		this.walletService = walletService;
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public Page<Bet>  getAllBets(UUID uuid, TransactionStatusEnum status, Pageable pageable) {
		var wallet = walletService.getWallet(uuid);
		var transactions = betRepository.findAllByWalletAndStatus(wallet,status,pageable);
		return transactions;
		
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public Page<Bet>  getAllBets(UUID uuid, Pageable pageable) {
		var wallet = walletService.getWallet(uuid);
		var transactions = betRepository.findAllByWallet(wallet,pageable);
		return transactions;
		
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public Page<Bet>  getAllBetsOpened(UUID uuid, Pageable pageable) {
		var wallet = walletService.getWallet(uuid);
		var transactions = betRepository.findAllByWalletAndStatus(wallet, TransactionStatusEnum.OPEN,pageable);
		return transactions;
		
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public Page<Bet>  getAllBetsClosed(UUID uuid, Pageable pageable) {
		var wallet = walletService.getWallet(uuid);
		var transactions = betRepository.findAllByWalletAndStatus(wallet, TransactionStatusEnum.CLOSED,pageable);
		return transactions;
		
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public Page<Bet>  getAllBetsCanceled(UUID uuid, Pageable pageable) {
		var wallet = walletService.getWallet(uuid);
		var transactions = betRepository.findAllByWalletAndStatus(wallet, TransactionStatusEnum.CANCELLED,pageable);
		return transactions;
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public Page<Bet>  getAllBetsPendingConfirmation(UUID uuid, Pageable pageable) {
		var wallet = walletService.getWallet(uuid);
		var transactions = betRepository.findAllByWalletAndStatus(wallet, TransactionStatusEnum.PENDING_CONFIRMATION,pageable);
		return transactions;
	}
}
