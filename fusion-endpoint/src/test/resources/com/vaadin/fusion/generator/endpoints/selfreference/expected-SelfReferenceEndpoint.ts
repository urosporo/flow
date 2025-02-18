/**
 * This module is generated from SelfReferenceEndpoint.java
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 * @module SelfReferenceEndpoint
 */

// @ts-ignore
import client from './connect-client.default';
import type SelfReference from './com/vaadin/fusion/generator/endpoints/selfreference/SelfReference';

function _getModel(): Promise<SelfReference | undefined> {
  return client.call('SelfReferenceEndpoint', 'getModel');
}
export {
  _getModel as getModel,
};
