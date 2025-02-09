# -------------------------------------------------------------
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
# -------------------------------------------------------------

# Autogenerated By   : src/main/python/generator/generator.py
# Autogenerated From : scripts/builtin/miceApply.dml

from typing import Dict, Iterable

from systemds.operator import OperationNode, Matrix, Frame, List, MultiReturn, Scalar
from systemds.script_building.dag import OutputType
from systemds.utils.consts import VALID_INPUT_TYPES


def miceApply(X: Matrix,
              meta: Matrix,
              threshold: float,
              dM: Frame,
              betaList: List):
    """
    :param threshold: confidence value [0, 1] for robust imputation, values will only be imputed
    :param if: value has probability greater than threshold,
    :param only: categorical data
    :param verbose: Boolean value.
    :return: 'OperationNode' containing are represented with empty string i.e ",," in csv file   & n are storing continuos/numeric data and variables with  & storing categorical data 
    """
    params_dict = {'X': X, 'meta': meta, 'threshold': threshold, 'dM': dM, 'betaList': betaList}
    return Matrix(X.sds_context,
        'miceApply',
        named_input_nodes=params_dict)
