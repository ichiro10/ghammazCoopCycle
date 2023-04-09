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

describe('Association e2e test', () => {
  const associationPageUrl = '/association';
  const associationPageUrlPattern = new RegExp('/association(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const associationSample = { idAsso: 97462, nomAsso: 'Pula' };

  let association;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/associations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/associations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/associations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (association) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/associations/${association.id}`,
      }).then(() => {
        association = undefined;
      });
    }
  });

  it('Associations menu should load Associations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('association');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Association').should('exist');
    cy.url().should('match', associationPageUrlPattern);
  });

  describe('Association page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(associationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Association page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/association/new$'));
        cy.getEntityCreateUpdateHeading('Association');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', associationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/associations',
          body: associationSample,
        }).then(({ body }) => {
          association = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/associations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [association],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(associationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Association page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('association');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', associationPageUrlPattern);
      });

      it('edit button click should load edit Association page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Association');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', associationPageUrlPattern);
      });

      it('edit button click should load edit Association page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Association');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', associationPageUrlPattern);
      });

      it('last delete button click should delete instance of Association', () => {
        cy.intercept('GET', '/api/associations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('association').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', associationPageUrlPattern);

        association = undefined;
      });
    });
  });

  describe('new Association page', () => {
    beforeEach(() => {
      cy.visit(`${associationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Association');
    });

    it('should create an instance of Association', () => {
      cy.get(`[data-cy="idAsso"]`).type('75580').should('have.value', '75580');

      cy.get(`[data-cy="nomAsso"]`).type('Iowa').should('have.value', 'Iowa');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        association = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', associationPageUrlPattern);
    });
  });
});
