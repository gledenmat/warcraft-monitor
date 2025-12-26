package fr.gledenmat.warcraftmonitor.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WarcraftTokenConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public WarcraftTokenConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${warcraft.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTokenPrice(String message) {
        log.info("📥 CONSUMER : Reçu de Kafka <{}>", message);

        // PLUS BESOIN DE SAUVEGARDER ICI
        // C'est le BlizzardService qui a déjà enregistré le prix dans le Repository.
        
        // --- DIFFUSION WEBSOCKET (Temps réel) ---
        // On transfère simplement le message aux navigateurs (Angular)
        messagingTemplate.convertAndSend("/topic/price", message);
        
        log.debug("📡 WEBSOCKET : Diffusé aux clients Angular");
    }
}