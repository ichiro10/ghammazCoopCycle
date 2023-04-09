import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Produit e2e test', () => {
  const produitPageUrl = '/produit';
  const produitPageUrlPattern = new RegExp('/produit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const produitSample = { idProd: 13663, nomProd: 'technologies South Architect', prixProd: 9483 };

  let produit;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/produits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/produits').as('postEntityRequest');
    cy.intercept('DELETE', '/api/produits/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (produit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/produits/${produit.id}`,
      }).then(() => {
        produit = undefined;
      });
    }
  });

  it('Produits menu should load Produits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('produit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Produit').should('exist');
    cy.url().should('match', produitPageUrlPattern);
  });

  describe('Produit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(produitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Produit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/produit/new$'));
        cy.getEntityCreateUpdateHeading('Produit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', produitPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/produits',
          body: produitSample,
        }).then(({ body }) => {
          produit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/produits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [produit],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(produitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Produit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('produit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', produitPageUrlPattern);
      });

      it('edit button click should load edit Produit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Produit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', produitPageUrlPattern);
      });

      it('edit button click should load edit Produit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Produit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', produitPageUrlPattern);
      });

      it('last delete button click should delete instance of Produit', () => {
        cy.intercept('GET', '/api/produits/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('produit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', produitPageUrlPattern);

        produit = undefined;
      });
    });
  });

  describe('new Produit page', () => {
    beforeEach(() => {
      cy.visit(`${produitPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Produit');
    });

    it('should create an instance of Produit', () => {
      cy.get(`[data-cy="idProd"]`).type('47817').should('have.value', '47817');

      cy.get(`[data-cy="nomProd"]`).type('invoice deposit disintermediate').should('have.value', 'invoice deposit disintermediate');

      cy.get(`[data-cy="prixProd"]`).type('37355').should('have.value', '37355');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        produit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', produitPageUrlPattern);
    });
  });
});
