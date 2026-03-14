package no.uib.inf112.records;

import no.uib.inf112.interfaces.IEnemy;

public record ShotDestination(double x, double y, IEnemy enemy) {
}