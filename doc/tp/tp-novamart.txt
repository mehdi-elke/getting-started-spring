= TP Microservices - Gestion de Commandes en Ligne

== 1. Introduction

Bienvenue dans ce TP fil rouge. Vous allez développer une application back-end composée de microservices en Java avec Spring Boot. Le sujet simule la gestion des commandes d'une entreprise de e-commerce fictive : **NovaMart**.

Chaque équipe sera responsable d'un ou plusieurs modules avec une charge de travail équilibrée.

Durée totale : Environ 10 heures (+ restitution)

== 2. Fonctionnement Général

Lorsqu'un client souhaite passer commande :

1. Vérification de la disponibilité des produits par zone.
2. Proposition des moyens de paiement.
3. Paiement de la commande.
4. Validation de la commande.
5. Passage de la commande par différents états jusqu'à la livraison.

== 3. Architecture Globale

[mermaid]
----
flowchart TD
    Client --> OrderManagerService
    OrderManagerService --> CatalogService
    OrderManagerService --> InventoryService
    OrderManagerService --> PaymentService
    OrderManagerService --> FulfillmentService
    FulfillmentService --> Transporter (simulé)
    Transporter --> FulfillmentService
----

[mermaid]
----
sequenceDiagram
    participant Client
    participant Order Manager
    participant Inventory Service
    participant Payment Service
    participant Fulfillment Service

    Client ->> Order Manager: Demande création de commande (produits + adresse)
    Order Manager ->> Inventory Service: Vérifie stock et possibilité de livraison
    Inventory Service -->> Order Manager: OK ou KO

    alt Stock ou livraison indisponible
        Order Manager -->> Client: Refus création de commande
    else Stock et livraison OK
        Order Manager -->> Client: Demande paiement
        Client ->> Payment Service: Choix moyen de paiement + paiement
        Payment Service -->> Client: Paiement OK/KO
        alt Paiement KO
            Order Manager -->> Client: Refus création de commande
        else Paiement OK
            Order Manager ->> Order Manager: Transition CREATED -> VALIDATED
            Order Manager ->> Fulfillment Service: Demande préparation
            Fulfillment Service -->> Order Manager: Préparation en cours
            Order Manager ->> Client: Confirmation commande validée
        end
    end
----


== 4. Cycle de Vie d'une Commande

| État          | Déclencheur                              | Description                     |
|-----------------|------------------------------------------|---------------------------------|
| CREATED         | Création, vérification stock/zone       | Commande créée                |
| VALIDATED       | Paiement validé                         | Commande validée, à préparer   |
| IN_PREPARATION  | Fulfillment lance la préparation         | Préparation en entrepôt         |
| IN_DELIVERY     | Fulfillment termine préparation          | Transmise au transporteur        |
| IN_TRANSIT      | Event du Transporter                     | En cours d'acheminement          |
| FULFILLED       | Event du Transporter                     | Livrée au client                 |

== 5. Modules

=== Order Manager Service
- Créer une commande (vérification stock / disponibilité livraison)
- Gérer l'état CREATED → VALIDATED
- Interface entre les différents services

=== Catalog Service
- Gestion CRUD des produits
- Stocke les informations produits (nom, prix, description)

=== Inventory Service
- Gère le stock des produits par **zone**.
- Décrémente le stock lors de commande validée.
- Refuse si stock insuffisant au moment de la commande.

=== Payment Service
- Simule différents moyens de paiement.
- Valide ou refuse un paiement.

=== Fulfillment Service
- Lance la préparation de commande après validation.
- Passe commande en livraison.
- Reçoit les événements de Transporter (IN_TRANSIT, FULFILLED).

== 6. Règles Fonctionnelles

- Une commande peut contenir **plusieurs produits**.
- La vérification stock/zone se fait avant paiement.
- Si le **stock devient insuffisant** avant le paiement, commande refusée.
- Si le **paiement échoue**, la commande n'est pas créée.

== 7. Simulation Transporteur

- Simulé via des appels HTTP sur Fulfillment Service :
    - `/shipment/start` : passage en `IN_TRANSIT`
    - `/shipment/delivered` : passage en `FULFILLED`

== 8. Contrats d'Interface

=== Commandes (Order Manager)
- `POST /orders/validate`
  - Entrée : Créer une commande (produits + adresse de livraison)
  - Sortie : Commande en CREATED si stock OK
- `POST /orders/pay`
  - Entrée : Informations paiement
  - Sortie : Commande VALIDATED ou refusée

=== Produits (Catalog)
- CRUD : `/products`

=== Stocks (Inventory)
- `GET /inventory/check`
  - Vérifier la disponibilité par zone.
- `POST /inventory/reserve`
  - Réserver les stocks pour une commande validée.

=== Paiement (Payment)
- `POST /payment/process`
  - Simuler un paiement

=== Fulfillment
- `POST /fulfillment/start`
  - Lancer la préparation.
- `POST /fulfillment/ready`
  - Terminer préparation et passer en livraison.
- `POST /shipment/start`
  - Marquer IN_TRANSIT.
- `POST /shipment/delivered`
  - Marquer FULFILLED.

== 9. Entités Principales

=== Commande
```json
{
  "id": "uuid",
  "customerId": "uuid",
  "deliveryAddress": { "zone": "string", ... },
  "contact": { "email": "string", "phoneNumber": "XXXXX"},
  "orderLines": [
    { "productId": "uuid", "quantity": number }
  ],
  "status": "CREATED | VALIDATED | IN_PREPARATION | IN_DELIVERY | IN_TRANSIT | FULFILLED"
}
```

=== Produit
```json
{
  "id": "uuid",
  "name": "string",
  "description": "string",
  "price": number
}
```

=== Stock
```json
{
  "productId": "uuid",
  "zone": "string",
  "availableQuantity": number
}
```

=== Paiement
```json
{
  "orderId": "uuid",
  "amount": number,
  "paymentMethod": "CREDIT_CARD | PAYPAL | ..."
}
```

== 10. Contraintes Techniques

- Java 17+
- Spring Boot 3+
- Base de données PostgreSQL
- API REST
- Validation des entrées
- Gestion d'états et erreurs
- OpenAPI (Swagger)

== 11. Restitution Attendue

- Chaque équipe présente son module.
- Démonstration d'un passage complet de commande (du créé au livré).
- Bonus : OpenAPI documenté et Postman ou Swagger prêt à démontrer.

---