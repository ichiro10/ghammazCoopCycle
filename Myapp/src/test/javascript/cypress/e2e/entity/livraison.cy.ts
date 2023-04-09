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

describe('Livraison e2e test', () => {
  const livraisonPageUrl = '/livraison';
  const livraisonPageUrlPattern = new RegExp('/livraison(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const livraisonSample = {
    idLivraison: 36712,
    prixLivraison: 51290,
    duree: '2023-04-09T06:14:43.117Z',
    adresseLivraison: 'iterate Cotton Representative',
  };

  let livraison;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/livraisons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/livraisons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/livraisons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (livraison) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/livraisons/${livraison.id}`,
      }).then(() => {
        livraison = undefined;
      });
    }
  });

  it('Livraisons menu should load Livraisons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('livraison');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Livraison').should('exist');
    cy.url().should('match', livraisonPageUrlPattern);
  });

  describe('Livraison page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(livraisonPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Livraison page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/livraison/new$'));
        cy.getEntityCreateUpdateHeading('Livraison');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', livraisonPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/livraisons',
          body: livraisonSample,
        }).then(({ body }) => {
          livraison = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/livraisons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [livraison],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(livraisonPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Livraison page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('livraison');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', livraisonPageUrlPattern);
      });

      it('edit button click should load edit Livraison page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Livraison');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', livraisonPageUrlPattern);
      });

      it('edit button click should load edit Livraison page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Livraison');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', livraisonPageUrlPattern);
      });

      it('last delete button click should delete instance of Livraison', () => {
        cy.intercept('GET', '/api/livraisons/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('livraison').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', livraisonPageUrlPattern);

        livraison = undefined;
      });
    });
  });

  describe('new Livraison page', () => {
    beforeEach(() => {
      cy.visit(`${livraisonPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Livraison');
    });

    it('should create an instance of Livraison', () => {
      cy.get(`[data-cy="idLivraison"]`).type('40046').should('have.value', '40046');

      cy.get(`[data-cy="prixLivraison"]`).type('66836').should('have.value', '66836');

      cy.get(`[data-cy="duree"]`).type('2023-04-09T14:52').blur().should('have.value', '2023-04-09T14:52');

      cy.get(`[data-cy="adresseLivraison"]`).type('Practical cross-media').should('have.value', 'Practical cross-media');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        livraison = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', livraisonPageUrlPattern);
    });
  });
});
